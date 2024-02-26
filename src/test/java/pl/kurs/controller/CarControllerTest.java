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
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreatCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    @Test
    void shouldAddCar() throws Exception {
        // Given
        CreatCarCommand command = new CreatCarCommand("Porshe", "panamera", "Pb");
        String json = objectMapper.writeValueAsString(command);

        // When & Then
        String responseJson = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Porshe"))
                .andExpect(jsonPath("$.model").value("panamera"))
                .andExpect(jsonPath("$.fuelType").value("Pb"))
                .andReturn()
                .getResponse()
                .getContentAsString();



        // Assert
        Car savedCar = objectMapper.readValue(responseJson, Car.class);
        Car recentlyAdded = carRepository.findById(savedCar.getId()).get();

        Assertions.assertEquals("Porshe", savedCar.getBrand());
        Assertions.assertEquals("panamera", savedCar.getModel());
        Assertions.assertEquals("Pb", savedCar.getFuelType());
    }

    @Test
    void shouldEditCar() throws Exception {
        // Given
        Car car = new Car("ToPut", "ToPutModel", "Pb");
        carRepository.saveAndFlush(car);

        EditCarCommand updatedCommand = new EditCarCommand();
        updatedCommand.setBrand("UpdatedBrand");
        updatedCommand.setModel("UpdatedModel");
        updatedCommand.setFuelType("UpdatedFuelType");
        String json = objectMapper.writeValueAsString(updatedCommand);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/" + car.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("UpdatedBrand"))
                .andExpect(jsonPath("$.model").value("UpdatedModel"))
                .andExpect(jsonPath("$.fuelType").value("UpdatedFuelType"));

        // Assert
        Car updatedCar = carRepository.findById(car.getId()).get();
        Assertions.assertNotNull(updatedCar);
        Assertions.assertEquals("UpdatedBrand", updatedCar.getBrand());
        Assertions.assertEquals("UpdatedModel", updatedCar.getModel());
        Assertions.assertEquals("UpdatedFuelType", updatedCar.getFuelType());
    }

    @Test
    void shouldRetrieveCar() throws Exception {
        // Given
        Car car = new Car("ToRetrieve", "ToRetrieveModel", "Pb");
        carRepository.saveAndFlush(car);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/" + car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("ToRetrieve"))
                .andExpect(jsonPath("$.model").value("ToRetrieveModel"))
                .andExpect(jsonPath("$.fuelType").value("Pb"));
    }

    // testy do delete, do patch, testy do dodania i usuniecia samochodu z garazu
}
