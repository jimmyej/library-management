package com.libraryapp.services;

import com.libraryapp.dtos.requests.BookRequest;
import com.libraryapp.entities.Book;

import java.util.List;

public interface BookService {

    Book registerBook(BookRequest book);

    List<Book> getBooksByTitle(String title);

    List<Book> getBooksByGender(String gender);

    List<Book> getBooksByAuthor(String author);

    List<Book> getBooksByIsActivated(String activated);

    Book getBookByIsbnCode(String isbnCode);

    List<Book> getBooksByLanguage(String language);

    Book editBook(BookRequest book);

    boolean removeBookByIsbnCode(String isbnCode);

    boolean removeBookById(int bookId);

}
