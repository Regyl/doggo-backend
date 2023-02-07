package ru.doggo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Boolean published;

    @Column
    private String content;

    @Column
    private LocalDateTime publishedOn;

    @Column
    private LocalDateTime updatedOn;

    public Post(String title, String description, Boolean published, String content) {
        this.title = title;
        this.description = description;
        this.published = published;
        this.content = content;
        this.publishedOn = LocalDateTime.now();
    }

    public Post() {

    }
}
