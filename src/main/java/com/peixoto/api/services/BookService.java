package com.peixoto.api.services;

import com.peixoto.api.dto.NewBookDTO;
import com.peixoto.api.dto.UpdateBookDTO;
import com.peixoto.api.entities.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.mapper.BookMapper;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    public List<Book> listAllBooks() {
        if(bookRepository.findAll() == null) {
            throw new NullPointerException("There is no books");
        }
        return bookRepository.findAll();
    }

    public Book searchBookById(long id) {
        return bookRepository.findById(id)
            .orElseThrow(()->new BadRequestException("Book not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public Book save(NewBookDTO newBookDTO) {
        var book = BookMapper.INSTANCE.toBook(newBookDTO);
        categoryRepository.findById(book.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        return bookRepository.save(book);
    }

    public void remove(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    public void replace(UpdateBookDTO book) {
        Book savedBook = searchBookById(book.getId());

        savedBook.setTitle(book.getTitle());
//        savedBook.setBookCategory(book.getBookCategory());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setId(book.getId());

        bookRepository.save(savedBook);
    }

}
