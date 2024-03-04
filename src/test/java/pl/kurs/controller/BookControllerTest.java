package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.Main;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldAddBook() throws Exception {
        // Given
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("Testowa Książka", "TESTOWA", author.getId());
        String json = objectMapper.writeValueAsString(command);

        // When-Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Testowa Książka"))
                .andExpect(jsonPath("$.category").value("TESTOWA"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void shouldEditeBook() throws Exception {
        // Given
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("Testowa Książka", "TESTOWA", author.getId());
        String json = objectMapper.writeValueAsString(command);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        int bookId = bookRepository.findAll().get(0).getId(); // Pobieramy ID dodanej książki

        // When
        String editJson = "{\n" +
                "    \"title\": \"Nowy tytuł\",\n" +
                "    \"category\": \"Nowa kategoria\",\n" +
                "    \"available\": false\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editJson))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nowy tytuł"))
                .andExpect(jsonPath("$.category").value("Nowa kategoria"))
                .andExpect(jsonPath("$.available").value(false));





    }
    @Test
    void shouldRetrieveBook() throws Exception {
        // Given
        Author author = authorRepository.findAll().get(0);
        Book book = new Book("ToRetrieve", "ToRetrieveCategory", true, author);
        bookRepository.saveAndFlush(book);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("ToRetrieve"))
                .andExpect(jsonPath("$.category").value("ToRetrieveCategory"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        // Given
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("Testowa Książka", "TESTOWA", author.getId());
        String json = objectMapper.writeValueAsString(command);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        int bookId = bookRepository.findAll().get(0).getId(); // Pobieramy ID dodanej książki

        // When-Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }


    @Test
    public void shouldPatchBook() throws Exception {
        // Given
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("Testowa Książka", "TESTOWA", author.getId());
        String json = objectMapper.writeValueAsString(command);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        int bookId = bookRepository.findAll().get(0).getId(); // Pobieramy ID dodanej książki

        // When
        String patchJson = "{\n" +
                "    \"title\": \"Nowy tytuł\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nowy tytuł"));

    }

}
