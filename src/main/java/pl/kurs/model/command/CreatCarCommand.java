package pl.kurs.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatCarCommand {
    private String brand;
    private String model;
    private String fuelType;
}
