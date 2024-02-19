package pl.kurs.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGarageCommand {
    private Integer places;
    private String address;
    private Boolean isLpgAllowed;
}
