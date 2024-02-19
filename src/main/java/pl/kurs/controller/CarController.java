package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.ResourceNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreatCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cars")
@Slf4j
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;
    private final GarageRepository garageRepository;

    @PostConstruct
    public void init() {
        garageRepository.saveAndFlush(new Garage(2, "Krotka", false));
       garageRepository.saveAndFlush(new Garage(1, "Dluga", true));


        carRepository.saveAndFlush(new Car("BMW", "M3", "Pb"));
        carRepository.saveAndFlush(new Car("BMW", "5", "ON"));
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(carRepository.findAll().stream().map(CarDto::from).toList());
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody CreatCarCommand command) {
        Car car = carRepository.saveAndFlush(new Car(command.getBrand(), command.getModel(), command.getFuelType()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CarDto.from(car));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> findCar(@PathVariable int id) {
        return ResponseEntity.ok(CarDto.from(carRepository.findById(id).orElseThrow(CarNotFoundException::new)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarDto> deleteCar(@PathVariable int id) {
        carRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> editCar(@PathVariable int id, @RequestBody EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return ResponseEntity.status(HttpStatus.OK).body(CarDto.from(carRepository.saveAndFlush(car)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarDto> editCarPartially(@PathVariable int id, @RequestBody EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        return ResponseEntity.status(HttpStatus.OK).body(CarDto.from(carRepository.saveAndFlush(car)));
    }
}