package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Component
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_title", length = 500)
    private String title;
    private String category;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(String title, String category, boolean available, Author author) {
        this.title = title;
        this.category = category;
        this.available = available;
        this.author = author;
//        author.getBooks().add(this);
    }
}
