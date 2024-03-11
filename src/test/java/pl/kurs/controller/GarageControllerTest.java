package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.Main;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.repository.GarageRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class GarageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageRepository garageRepository;

    @Test
    void shouldAddGarage() throws Exception {
        CreateGarageCommand command = new CreateGarageCommand(5, "Test Address", true);
        String json = objectMapper.writeValueAsString(command);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(command.getPlaces()))
                .andExpect(jsonPath("$.address").value(command.getAddress()))
                .andExpect(jsonPath("$.lpgAllowed").value(command.getIsLpgAllowed()));
    }

    @Test
    void shouldPatchGarage() throws Exception {
        // Create a new Garage object and save it to the repository
        Garage garage =  garageRepository.saveAndFlush(new Garage(30, "Warszawa", true));

        // Get the ID of the saved garage
        int garageId = garage.getId();

        EditGarageCommand command = new EditGarageCommand(50, null, null);

        // Convert the command object to JSON
        String json = objectMapper.writeValueAsString(command);

        // Perform a PATCH request to the /api/v1/garages/{id} endpoint
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/garages/" + garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId))
                .andExpect(jsonPath("$.address").value("Warszawa"))
                .andExpect(jsonPath("$.places").value(50))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
    }

    @Test
    void shouldRetrieveGarage() throws Exception {
        int garageId = garageRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/garages/" + garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId));
    }
    @Test
    void shouldDeleteGarage() throws Exception {
        int garageId = garageRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/garages/" + garageId))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(garageRepository.findById(garageId).isEmpty());
    }




}
