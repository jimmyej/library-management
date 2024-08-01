package com.libraryapp.controllers;

import com.libraryapp.entities.Book;
import com.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/titles/{title}")
    @ResponseBody
    List<Book> getBooksByTitle(@PathVariable String title){
        return bookService.getBooksByTitle(title);
    }

    @GetMapping("/genders/{gender}")
    @ResponseBody
    List<Book> getBooksByGender(@PathVariable String gender){
        return bookService.getBooksByGender(gender);
    }

    @GetMapping("/authors/{author}")
    @ResponseBody
    List<Book> getBooksByAuthor(@PathVariable String author){
        return bookService.getBooksByAuthor(author);
    }

    @GetMapping("/codes/{isbnCode}")
    @ResponseBody
    Book getBookByIsbnCode(@PathVariable String isbnCode){
        return bookService.getBookByIsbnCode(isbnCode);
    }

    @GetMapping("/languages/{language}")
    @ResponseBody
    List<Book> getBooksByLanguage(@PathVariable String language){
        return bookService.getBooksByLanguage(language);
    }

    @DeleteMapping("/codes/{isbnCode}")
    boolean removeBookByIsbnCode(@PathVariable String isbnCode){
        return bookService.removeBookByIsbnCode(isbnCode);
    }

    @PostMapping("")
    @ResponseBody
    Book registerBook(@RequestBody Book book){
        return bookService.registerBook(book);
    }

    @PutMapping("")
    @ResponseBody
    Book editBook(@RequestBody Book book){
        return bookService.editBook(book);
    }

    @DeleteMapping("/ids/{bookId}")
    boolean removeBookById(@PathVariable int bookId){
        return bookService.removeBookById(bookId);
    }

    @GetMapping("/status/{isActivated}")
    @ResponseBody
    List<Book> getBooksByIsActivated(@PathVariable boolean isActivated){
        return bookService.getBooksByIsActivated(isActivated);
    }
}
