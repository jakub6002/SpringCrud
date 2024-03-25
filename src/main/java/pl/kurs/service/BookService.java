package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Book book;
    private final EntityManager entityManager;

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
        return bookRepository.save(new Book(command.getTitle(), command.getCategory(), true, authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new)));
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


    @Transactional
    public void importBook(InputStream inputStream) {
        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines()
                    .map(line -> line.split(","))
                    .map(args -> new CreateBookCommand(args))
                    .peek(command -> countTime(counter, start))
                    .forEach(this::save);
        } catch (IOException e) {
        }
    }


    private void countTime(AtomicInteger counter, AtomicLong start) {
        if (counter.incrementAndGet() % 10000 == 0) {
            log.info("Imported: {} in {} ms", counter, (System.currentTimeMillis() - start.get()));
            start.set(System.currentTimeMillis());
            entityManager.clear();
        }
    }


    // sprawdzcie sobie na starcie jak to u was idzie i postarajcie poprawic ten wynik
    // 1) 20k-40k na sekunde mniej wiecej
    // 2) zeby zuzycie pamieci sie nie zwiekszylo
    // 3) wszystko albo nic
    // 4) testy jakies



}
