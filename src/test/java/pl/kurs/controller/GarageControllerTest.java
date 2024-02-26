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
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.repository.GarageRepository;

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
    void shouldEditGarage() throws Exception {
        // Assuming there is at least one garage in the database
        int garageId = garageRepository.findAll().get(0).getId();

        CreateGarageCommand command = new CreateGarageCommand(10, "Updated Address", false);
        String json = objectMapper.writeValueAsString(command);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/garages/" + garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId))
                .andExpect(jsonPath("$.places").value(command.getPlaces()))
                .andExpect(jsonPath("$.address").value(command.getAddress()))
                .andExpect(jsonPath("$.lpgAllowed").value(command.getIsLpgAllowed()));
    }

    @Test
    void shouldRetrieveGarage() throws Exception {
        int garageId = garageRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/garages/" + garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId));
    }
}
