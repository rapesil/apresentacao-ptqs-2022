package com.peixoto.api;

import com.peixoto.api.entities.Book;
import com.peixoto.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookHelpers {

    @Autowired
    private BookRepository repository;

    public void deleteByIdIfExists(long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            repository.deleteById(id);
        }
    }
}
