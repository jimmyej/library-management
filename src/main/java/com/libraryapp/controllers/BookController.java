package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.BookRequest;
import com.libraryapp.entities.Book;
import com.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    BookService bookService;

    @Autowired
    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/titles/{title}")
    ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        List<Book> books = bookService.getBooksByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genders/{gender}")
    ResponseEntity<List<Book>> getBooksByGender(@PathVariable String gender) {
        List<Book> books = bookService.getBooksByGender(gender);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/authors/{author}")
    ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/codes/{isbnCode}")
    ResponseEntity<Book> getBookByIsbnCode(@PathVariable String isbnCode) {
        Book book = bookService.getBookByIsbnCode(isbnCode);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/languages/{language}")
    ResponseEntity<List<Book>> getBooksByLanguage(@PathVariable String language) {
        List<Book> books = bookService.getBooksByLanguage(language);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/codes/{isbnCode}")
    ResponseEntity<Boolean> removeBookByIsbnCode(@PathVariable String isbnCode) {
        boolean removed = bookService.removeBookByIsbnCode(isbnCode);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    ResponseEntity<Book> registerBook(@RequestBody BookRequest book) {
        Book newBook = bookService.registerBook(book);
        if (newBook != null) {
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    @ResponseBody
    ResponseEntity<Book> editBook(@RequestBody Book book) {
        Book newBook = bookService.editBook(book);
        if (newBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("/ids/{bookId}")
    ResponseEntity<Boolean> removeBookById(@PathVariable int bookId) {
        boolean removed = bookService.removeBookById(bookId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    ResponseEntity<List<Book>> getBooksByIsActivated(@RequestParam(required = false) String activated) {
        List<Book> books = bookService.getBooksByIsActivated(activated);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
}
