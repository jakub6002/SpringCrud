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
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;

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
        command.setId(1);
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
        EditAuthorCommand command = new EditAuthorCommand();
        command.setFirstName("Jane");
        command.setLastName("Doe");
        mockMvc.perform(patch("/api/v1/authors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }
}