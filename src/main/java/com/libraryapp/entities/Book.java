package com.libraryapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table( name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(length = 100)
    private String title;
    @Column(length = 50)
    private String gender;
    @Column(length = 50)
    private String editorial;
    private short pages;
    @Column(length = 20)
    private String language;
    @Column(length = 50)
    private String author;
    private LocalDate publishedAt;

    @Column(name = "isbn_code", length = 32, unique = true)
    private String isbnCode;
    @Column(name = "is_activated")
    private boolean activated;
}
