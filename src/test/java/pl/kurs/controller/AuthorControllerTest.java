package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.repository.AuthorRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
// w mainie na razie mamy jedna konfiguracjhe, skanowanie i auokonfiguracja, testy beda uruchamialy caly kontekst springowuy, nie sa to testy jednostkowe
@AutoConfigureMockMvc // zebysmy z kontenera springowego mogli pozyskac obiekty, taki programistyczny postman
@ActiveProfiles("tests")
public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldAddAuthor() throws Exception {
        CreateAuthorCommand command = new CreateAuthorCommand("John", "Doe", 1800, 1850);
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());
    }

    @Test
    public void ShouldEditAuthor() throws Exception {

        EditAuthorCommand command = new EditAuthorCommand();
        command.setFirstName("Jane");
        command.setLastName("Doe");
        command.setBirthDate(1805);
        command.setDeathDate(1855);
        mockMvc.perform(put("/api/v1/authors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRetrieveAuthor() throws Exception {
        mockMvc.perform(get("/api/v1/authors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.birthDate").exists())
                .andExpect(jsonPath("$.deathDate").exists());
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldPatchAuthor() throws Exception {
        // Create a new Author and save it to the repository
        Author author = authorRepository.saveAndFlush(new Author("John", "Doe", 1800, 1850));

        // Get the ID of the saved author
        int authorId = author.getId();

        EditAuthorCommand command = new EditAuthorCommand();
        command.setFirstName("Jane");
        command.setLastName("Doe");

        mockMvc.perform(patch("/api/v1/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorId))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()));
    }
}