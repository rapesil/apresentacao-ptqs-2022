package com.peixoto.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.peixoto.api.dto.NewBook;
import com.peixoto.api.dto.UpdateBook;
import com.peixoto.api.entities.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.mapper.BookMapper;
import com.peixoto.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public List<Book> findAll() {
        if(bookRepository.findAll() == null) {
            throw new NullPointerException("There is no books");
        }
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
            .orElseThrow(()->new BadRequestException("Book not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public Book save(NewBook newBook) {
        var book = BookMapper.INSTANCE.toBook(newBook);

        return bookRepository.save(book);
    }

    @Transactional(rollbackOn = Exception.class)
    public Book saveAndUploadToS3(NewBook newBook, MultipartFile multipartFile) throws Exception {
        amazonS3.getUrl(bucketName, "cover");

        var filename = multipartFile.getOriginalFilename();
        var key = "cover/" + filename;
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
        multipartFile.transferTo(file);

        amazonS3.putObject(bucketName, key, file);

        var book = BookMapper.INSTANCE.toBook(newBook);
        book.setImagePath(key);

        return bookRepository.save(book);
    }

    public S3Object downloadFromS3(String imagePath) throws Exception {
        var s3Object = amazonS3.getObject(bucketName, imagePath);

        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream,
                new File("/Users/rafaelpeixoto/Desktop/downloadedFile.png"));
        return s3Object;
    }

    public void remove(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    public void replace(UpdateBook book) {
        Book savedBook = findById(book.getId());

        savedBook.setTitle(book.getTitle());
        savedBook.setBookCategory(book.getBookCategory());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setId(book.getId());

        bookRepository.save(savedBook);
    }

}
