package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EditGarageCommand {
    private Integer places;
    private String address;
    private Boolean isLpgAllowed;
}