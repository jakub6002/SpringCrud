package pl.kurs.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
@Slf4j
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;


    @GetMapping
    public ResponseEntity<List<AuthorDto>> findAll() {
        log.info("findAll()");
        return ResponseEntity.ok(authorService.findAll().stream().map(AuthorDto::from).toList());
    }

    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody CreateAuthorCommand command) {
        log.info("addAuthor({})", command);
        Author author = authorService.save(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthorDto.from(author));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findAuthor(@PathVariable int id) {
        log.info("findAuthor({})", id);
        return ResponseEntity.ok(AuthorDto.from(authorService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable int id) {
        log.info("deleteAuthor({})", id);
        authorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthor(@PathVariable int id, @RequestBody EditAuthorCommand command) {
        log.info("editAuthor({}, {})", id, command);
        Author author = authorService.edit(command);
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.from(author));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthorPartially(@PathVariable int id, @RequestBody EditAuthorCommand command) {
        log.info("editAuthorPartially({}, {})", id, command);
        Author author = authorService.partiallyEdit(command);
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.from(author));
    }

    @PatchMapping("/{id}/books/{bookId}")
    public ResponseEntity<AuthorDto> addBookToAuthor(@PathVariable int id, @PathVariable int bookId) {
        log.info("addBookToAuthor({}, {})", id, bookId);
        authorService.addBookToAuthor(id, bookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/books/{bookId}")
    public ResponseEntity<AuthorDto> removeBookFromAuthor(@PathVariable int id, @PathVariable int bookId) {
        log.info("removeBookFromAuthor({}, {})", id, bookId);
        authorService.removeBookFromAuthor(id, bookId);
        return ResponseEntity.ok().build();
    }
}
