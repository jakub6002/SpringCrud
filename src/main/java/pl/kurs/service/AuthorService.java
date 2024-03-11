package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        authorRepository.saveAndFlush(new Author("Adam", "Mickiewicz", 1798, 1855));
        authorRepository.saveAndFlush(new Author("Henryk", "Sienkiewicz", 1846, 1916));
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author save(CreateAuthorCommand command) {
        return authorRepository.saveAndFlush(new Author(command.getFirstName(), command.getLastName(), command.getBirthDate(), command.getDeathDate()));
    }

    public Author findById(int id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public void deleteById(int id) {
        authorRepository.deleteById(id);
    }

    public Author edit(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        author.setName(command.getFirstName());
        author.setSurname(command.getLastName());
        author.setBirthYear(command.getBirthDate());
        author.setDeathYear(command.getDeathDate());
        return authorRepository.saveAndFlush(author);
    }
   public Author partiallyEdit(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Optional.ofNullable(command.getFirstName()).ifPresent(author::setName);
        Optional.ofNullable(command.getLastName()).ifPresent(author::setSurname);
        Optional.ofNullable(command.getBirthDate()).ifPresent(author::setBirthYear);
        Optional.ofNullable(command.getDeathDate()).ifPresent(author::setDeathYear);
        return authorRepository.saveAndFlush(author);
    }

    public void addBookToAuthor(int authorId, int bookId) {
        Author author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        author.getBooks().add(bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new));
        authorRepository.saveAndFlush(author);
    }

    public void removeBookFromAuthor(int authorId, int bookId) {
        Author author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        author.getBooks().remove(bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new));
        authorRepository.saveAndFlush(author);
    }
}

