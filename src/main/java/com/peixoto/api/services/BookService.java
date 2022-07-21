package com.peixoto.api.services;

import com.peixoto.api.dto.NewBook;
import com.peixoto.api.dto.UpdateBook;
import com.peixoto.api.entities.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.mapper.BookMapper;
import com.peixoto.api.repository.BookRepository;
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
