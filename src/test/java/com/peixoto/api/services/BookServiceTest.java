package com.peixoto.api.services;

import com.peixoto.api.entities.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de unidade - Book Service")
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository mockBookRepository;

    @Test
    @DisplayName("Deve retornar todos os livros")
    void findALL_shouldReturnAllBooks() {
        Mockito.when(mockBookRepository.findAll())
                .thenReturn(Arrays.asList(new Book(), new Book()));

        assertThat(bookService.listAllBooks().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar livros (busca por todos)")
    void findALL_shouldThrowException_whenThereIsNoBooks() {
        Mockito.when(mockBookRepository.findAll())
            .thenReturn(null);

        assertThatThrownBy(() -> bookService.listAllBooks())
            .isInstanceOf(NullPointerException.class)
            .hasMessage("There is no books");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um livro que não existe")
    void findById_shouldThrowBadRequestException() {
        assertThatThrownBy(() -> bookService.searchBookById(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover livro que não existe")
    void remove_shouldThrowException_whenBookNotExists() {
        assertThatCode(() -> bookService.remove(1L)).doesNotThrowAnyException();

        verify(mockBookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve remover livro com sucesso")
    void remove_shouldRemoveBook() {
        createBook();

        assertThatCode(() -> bookService.remove(1L)).doesNotThrowAnyException();

        verify(mockBookRepository, times(1)).deleteById(1L);
    }

    private Book createBook() {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setAuthor("Rafael Peixoto");
        savedBook.setBookCategory("Software Test");
        savedBook.setTitle("Selenium WebDriver");
        return savedBook;
    }

}
