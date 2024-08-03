package com.libraryapp.services;

import com.libraryapp.entities.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooksByTitle(String title);
    List<Book> getBooksByGender(String gender);
    List<Book> getBooksByAuthor(String author);
    Book getBookByIsbnCode(String isbnCode);
    List<Book> getBooksByLanguage(String language);
    boolean removeBookByIsbnCode(String isbnCode);

    Book registerBook(Book book);
    Book editBook(Book book);
    boolean removeBookById(int bookId);
    List<Book> getBooksByIsActivated(String activated);
}
