package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "books")
@NoArgsConstructor
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author")
    private Set<Book> books = new HashSet<>();

    public Author(String name, String surname, Integer birthYear, Integer deathYear) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void remove(Book book) {
        books.remove(book);
        book.setAuthor(null);

    }


}
