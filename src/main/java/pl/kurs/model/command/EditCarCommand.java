package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EditCarCommand {

    private String brand;
    private String model;
    private String fuelType;

}
