package com.libraryapp.services;

import com.libraryapp.dtos.requests.BookRequest;
import com.libraryapp.entities.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooksByTitle(String title);
    List<Book> getBooksByGender(String gender);
    List<Book> getBooksByAuthor(String author);
    Book getBookByIsbnCode(String isbnCode);
    List<Book> getBooksByLanguage(String language);
    boolean removeBookByIsbnCode(String isbnCode);

    Book registerBook(BookRequest book);
    Book editBook(BookRequest book);
    boolean removeBookById(int bookId);
    List<Book> getBooksByIsActivated(String activated);
}
