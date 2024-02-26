package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreatCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static pl.kurs.model.Car.*;

@Service
@RequiredArgsConstructor
public class CarService {

    private final GarageRepository garageRepository;
    private final CarRepository carRepository;


    @PostConstruct
    public void init() {
        carRepository.saveAndFlush(new Car("BMW", "M2", "PB"));
        carRepository.saveAndFlush(new Car("Ferarri", "F8", "PB"));
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car save(CreatCarCommand command) {
        return carRepository.saveAndFlush(new Car(command.getBrand(), command.getModel(), command.getFuelType()));
    }

    public Optional<Car> findById(int id) {
        return carRepository.findById(id);
    }

    public void deleById(int id) {
        carRepository.deleteById(id);
    }


    public Car edit(int id, EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return carRepository.saveAndFlush(car);
    }

    public Car partiallyEdit(int id, EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        return carRepository.saveAndFlush(car);
    }
}



