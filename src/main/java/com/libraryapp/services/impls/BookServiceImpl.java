package com.libraryapp.services.impls;

import com.libraryapp.entities.Book;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.BookRepository;
import com.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBooksByGender(String gender) {
        return bookRepository.findByGender(gender);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public Book getBookByIsbnCode(String isbnCode) {
        return bookRepository.findByIsbnCode(isbnCode);
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    public boolean removeBookByIsbnCode(String isbnCode) {
        boolean isDeleted = false;
        try {
            boolean existsByIsbnCode = bookRepository.existsByIsbnCode(isbnCode);
            if(existsByIsbnCode){
                Book book = bookRepository.findByIsbnCode(isbnCode);
                book.setActivated(false);
                bookRepository.save(book);
                isDeleted = true;
            }
        } catch(Exception e){
            e.getCause();
        }
        return isDeleted;
    }

    public Book registerBook(Book book) {
        boolean existsByIsbnCode = bookRepository.existsByIsbnCode(book.getIsbnCode());
        if(!existsByIsbnCode){
            return bookRepository.save(book);
        }
        return null;
    }

    public Book editBook(Book book) {
        boolean existsById = bookRepository.existsById(book.getBookId());
        if(existsById){
            Book existingBook = bookRepository.findById(book.getBookId()).get();
            if(existingBook.getIsbnCode().equals(book.getIsbnCode())){
                return bookRepository.save(book);
            }
        }
        return null;
    }

    public boolean removeBookById(int bookId) {
        boolean isDeleted = false;
        try {
            boolean existById = bookRepository.existsById(bookId);
            if(existById){
                Book book = bookRepository.findById(bookId).get();
                book.setActivated(false);
                bookRepository.save(book);
                isDeleted = true;
            }
        } catch(Exception e){
            e.getCause();
        }
        return isDeleted;
    }

    public List<Book> getBooksByIsActivated(String activated) {
        if(activated != null){
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return bookRepository.findByActivated(status);
        } else {
            return (List<Book>) bookRepository.findAll();
        }
    }
}
