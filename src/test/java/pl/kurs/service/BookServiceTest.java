package pl.kurs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;
import pl.kurs.service.BookService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldFindBookByIdIfBookExists() {
        int bookId = 1;
        Author author = new Author("Kazimierz", "Wielki", 1900, 2000); // Assuming a constructor exists
        Book expectedBook = new Book("Ogniem i Mieczem", "Historical", true, author); // Assuming a constructor exists
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Optional<Book> actualBookOptional = bookService.findBookById(bookId);

        assertTrue(actualBookOptional.isPresent(), "Book should be found");
        Book actualBook = actualBookOptional.get();
        assertEquals(expectedBook, actualBook, "The found book should match the expected book");
        verify(bookRepository).findById(bookId);
    }

    @Test
    void shoouldFindBookByIdWhenBookDoesNotExist() {

        int bookId = 99;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(bookId).orElseThrow(BookNotFoundException::new), "Expected BookNotFoundException to be thrown when book is not found");
        verify(bookRepository).findById(bookId);
    }
}
