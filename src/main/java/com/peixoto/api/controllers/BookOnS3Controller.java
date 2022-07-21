package com.peixoto.api.controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.peixoto.api.dto.NewBook;
import com.peixoto.api.entities.Book;
import com.peixoto.api.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("s3")
public class BookOnS3Controller {

    @Autowired
    private final BookService bookService;

    @PostMapping(value = "/books",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE
            })
    public ResponseEntity<Book> save(@NotNull @Valid @RequestPart("book") NewBook book,
                                     @RequestPart("file") MultipartFile file)  throws Exception{
        return new ResponseEntity<>(bookService.saveAndUploadToS3(book, file), HttpStatus.CREATED);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> listBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/books/download")
    public ResponseEntity<S3Object> downloadFileFromS3(@RequestParam(name = "imagePath") String imagePath) throws Exception {
        return ResponseEntity.ok(bookService.downloadFromS3(imagePath));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>>listAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

}
