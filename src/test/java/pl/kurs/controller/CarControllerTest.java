package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import pl.kurs.repository.GarageRepository;
import pl.kurs.repository.CarRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
// w mainie na razie mamy jedna konfiguracjhe, skanowanie i auokonfiguracja, testy beda uruchamialy caly kontekst springowuy, nie sa to testy jednostkowe
@AutoConfigureMockMvc // zebysmy z kontenera springowego mogli pozyskac obiekty, taki programistyczny postman
@ActiveProfiles("tests")
class CarControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private GarageRepository garageRepository;


    @Test
    void shouldReturnSingleCar() throws Exception {
        Garage garage = garageRepository.findAllWithCars().get(0);
        int id = carRepository.saveAndFlush(new Car("Porshe", "panamera", "Pb", garage)).getId();
        postman.perform(MockMvcRequestBuilders.get("/api/v1/cars/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.brand").value("Porshe"))
                .andExpect(jsonPath("$.model").value("panamera"))
                .andExpect(jsonPath("$.fuelType").value("Pb"))
                .andExpect(jsonPath("$.garageId").value(garage.getId()));
    }

    @Test
    void shouldAddCar() throws Exception {
        Garage garage = garageRepository.findAll().get(0);
        CreatCarCommand command = new CreatCarCommand("Porshe", "panamera", "Pb", garage.getId());
        String json = objectMapper.writeValueAsString(command);


        String responseJson = postman.perform(MockMvcRequestBuilders.post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Porshe"))
                .andExpect(jsonPath("$.model").value("panamera"))
                .andExpect(jsonPath("$.fuelType").value("Pb"))
                .andExpect(jsonPath("$.garageId").value(garage.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Car saved = objectMapper.readValue(responseJson, Car.class);

        Car recentlyAdded = carRepository.findById(saved.getId()).get();

        postman.perform(MockMvcRequestBuilders.get("/api/v1/cars/" + recentlyAdded.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recentlyAdded.getId()))
                .andExpect(jsonPath("$.brand").value(recentlyAdded.getBrand()))
                .andExpect(jsonPath("$.model").value(recentlyAdded.getModel()))
                .andExpect(jsonPath("$.fuelType").value(recentlyAdded.getFuelType()))
                .andExpect(jsonPath("$.garageId").value(recentlyAdded.getGarage().getId()));


    }
//
//    @Test
//    void shouldDeleteCar() throws Exception {
//        Garage garage = garageRepository.findAll().get(0);
//        Car car = new Car("ToDelete", "ToDeleteModel", "Pb", garage);
//        carRepository.saveAndFlush(car);
//
//        postman.perform(MockMvcRequestBuilders.delete("/api/v1/cars/" + car.getId()))
//                .andExpect(status().isNoContent());
//
//        Assertions.assertFalse(carRepository.existsById(car.getId()));
//    }
//
//    @Test
//    void shouldPutCar() throws Exception {
//        Garage garage = garageRepository.findAll().get(0);
//        Car car = new Car("ToPut", "ToPutModel", "Pb", garage);
//        carRepository.saveAndFlush(car);
//
//        EditCarCommand updatedCommand = new EditCarCommand();
//        updatedCommand.setBrand("UpdatedBrand");
//        updatedCommand.setModel("UpdatedModel");
//        updatedCommand.setFuelType("UpdatedFuelType");
//
//        String json = objectMapper.writeValueAsString(updatedCommand);
//
//
//        postman.perform(MockMvcRequestBuilders.put("/api/v1/cars/" + car.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(car.getId()))
//                .andExpect(jsonPath("$.brand").value("UpdatedBrand"))
//                .andExpect(jsonPath("$.model").value("UpdatedModel"))
//                .andExpect(jsonPath("$.fuelType").value("UpdatedFuelType"));
//
//        Car updatedCar = carRepository.findById(car.getId()).orElse(null);
//
//        Assertions.assertNotNull(updatedCar);
//        Assertions.assertEquals("UpdatedBrand", updatedCar.getBrand());
//        Assertions.assertEquals("UpdatedModel", updatedCar.getModel());
//        Assertions.assertEquals("UpdatedFuelType", updatedCar.getFuelType());
//    }


}
