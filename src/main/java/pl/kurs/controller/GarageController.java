package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.service.GarageService;

import java.util.List;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;


    @GetMapping
    public ResponseEntity<List<GarageDto>> findAll() {
        log.info("findAll()");
        return ResponseEntity.ok(garageService.findAll().stream().map(GarageDto::from).toList());
    }

    @PostMapping
    public ResponseEntity<GarageDto> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        Garage garage = garageService.save(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(GarageDto.from(garage));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(GarageDto.from(garageService.findById(id).orElseThrow(GarageNotFoundException::new)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<GarageDto> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        garageService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<GarageDto> editGarage(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        Garage garage = garageService.edit(id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.from(garage));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GarageDto> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGaragePartially({}, {})", id, command);
        Garage garage = garageService.partiallyEdit(id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.from(garage));
    }

    @PatchMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> addCarToGarage(@PathVariable int id, @PathVariable int carId) {
        log.info("addCarToGarage({}, {})", id, carId);
        garageService.addCarToGarage(id, carId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> deleteCarFromGarage(@PathVariable int id, @PathVariable int carId) {
        log.info("deleteCatFromGarage({}, {})", id, carId);
        garageService.removeCarFromGarage(id, carId);
        return ResponseEntity.ok().build();
    }


}
