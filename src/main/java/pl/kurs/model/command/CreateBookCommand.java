package pl.kurs.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBookCommand {

    private String title;
    private String category;
    private Integer authorId;

}
