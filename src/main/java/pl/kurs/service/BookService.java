package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Book book;

    @PostConstruct
    public void init() {
        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wileki", 1900, 2000));

        bookRepository.saveAndFlush(new Book("W pustyni i w puszczy", "Powiesc", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "Powiesc", true, a1));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    public Book save(CreateBookCommand command) {
        return bookRepository.saveAndFlush(new Book(command.getTitle(), command.getCategory(), true, authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new)));
    }

    public Book edit(EditBookCommand command) {
        book.setTitle(command.getTitle());
        book.setCategory(command.getCategory());
        book.setAvailable(command.getAvailable());
        return bookRepository.saveAndFlush(book);
    }

    public Book partiallyEdit(EditBookCommand command) {
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        return bookRepository.saveAndFlush(book);
    }


}
