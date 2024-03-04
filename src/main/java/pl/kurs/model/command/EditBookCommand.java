package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditBookCommand {

    private String title;
    private String category;
    private Boolean available;

}
