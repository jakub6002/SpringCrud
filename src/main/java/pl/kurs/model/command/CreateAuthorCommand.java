package pl.kurs.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAuthorCommand {

        private String firstName;
        private String lastName;
        private Integer birthDate;
        private Integer deathDate;

}
