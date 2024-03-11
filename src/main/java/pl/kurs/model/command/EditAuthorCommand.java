package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EditAuthorCommand {
    private String firstName;
    private String lastName;
    private Integer birthDate;
    private Integer deathDate;
}
