package com.libraryapp.repositories;

import com.libraryapp.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    //CRUD= create, read, update, deplete
    List<Book> findByTitle(String title);
    List<Book> findByGender(String gender);
    List<Book> findByAuthor(String author);
    Book findByIsbnCode(String isbnCode);
    List<Book> findByLanguage(String language);
    List<Book> findByActivated(boolean isActivated);
    boolean existsByIsbnCode(String isbnCode);
}
