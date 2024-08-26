package com.libraryapp.services.impls;

import com.libraryapp.dtos.requests.BookRequest;
import com.libraryapp.entities.Book;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.BookRepository;
import com.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;

    @Autowired
    BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

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

        boolean existsByIsbnCode = bookRepository.existsByIsbnCode(isbnCode);

        if (existsByIsbnCode) {
            return bookRepository.findByIsbnCode(isbnCode);
        }
        return null;
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    public boolean removeBookByIsbnCode(String isbnCode) {
        boolean isDeleted = false;
        try {
            boolean existsByIsbnCode = bookRepository.existsByIsbnCode(isbnCode);
            if (existsByIsbnCode) {
                Book book = bookRepository.findByIsbnCode(isbnCode);
                book.setActivated(false);
                bookRepository.save(book);
                isDeleted = true;
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return isDeleted;
    }

    public Book registerBook(BookRequest book) {
        boolean existsByIsbnCode = bookRepository.existsByIsbnCode(book.getIsbnCode());
        if (!existsByIsbnCode) {
            return bookRepository.save(buildBookRequest(book,0));
        }
        return null;
    }
    private Book buildBookRequest(BookRequest book, int id){
        Book newBook = new Book();
        if(id > 0){
            newBook.setBookId(id);
        }
        newBook.setTitle(book.getTitle());
        newBook.setGender(book.getGender());
        newBook.setEditorial(book.getEditorial());
        newBook.setPages(book.getPages());
        newBook.setLanguage(book.getLanguage());
        newBook.setAuthor(book.getAuthor());
        newBook.setPublishedAt(book.getPublishedAt());
        newBook.setIsbnCode(book.getIsbnCode());
        newBook.setActivated(book.isActivated());
        return newBook;
    }

    public Book editBook(BookRequest book) {
        boolean existsById = bookRepository.existsById(book.getBookId());
        if (existsById) {
            Optional<Book> existingBook = bookRepository.findById(book.getBookId());
            if (existingBook.isPresent() && existingBook.get().getIsbnCode().equals(book.getIsbnCode())) {
                return bookRepository.save(buildBookRequest(book, book.getBookId()));
            }
            return null;
        }
        return null;
    }

    public boolean removeBookById(int bookId) {
        boolean isDeleted = false;
        try {
            boolean existById = bookRepository.existsById(bookId);
            if (existById) {
                Optional<Book> book = bookRepository.findById(bookId);
                if(book.isPresent()){
                    book.get().setActivated(false);
                    bookRepository.save(book.get());
                    isDeleted = true;
                }
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return isDeleted;
    }

    public List<Book> getBooksByIsActivated(String activated) {
        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return bookRepository.findByActivated(status);
        } else {
            return (List<Book>) bookRepository.findAll();
        }
    }
}
