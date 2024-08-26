package com.libraryapp.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookRequest {
    private int bookId;
    private String title;
    private String gender;
    private String editorial;
    private short pages;
    private String language;
    private String author;
    private LocalDate publishedAt;
    private String isbnCode;
    private boolean activated;
}
