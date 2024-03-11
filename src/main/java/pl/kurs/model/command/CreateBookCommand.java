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

    public CreateBookCommand(String[] args) {
        this.title = args[0];
        this.category = args[1];
        this.authorId = Integer.parseInt(args[2]);
    }
}
