package com.libraryapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.entities.Book;
import com.libraryapp.repositories.BookRepository;
import com.libraryapp.services.impls.BookServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookServiceImpl.class)
public class BookControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookRepository bookRepository;

    @Autowired
    ObjectMapper mapper;

    Book book1 = new Book(1, "Testing with Mockito", "Programming", "Editorial ABC", (short) 254, "ESP", "Pepito Perez", LocalDate.of(2021,1,1), "123456789", true);
    Book book2 = new Book(2, "Testing with Mockito", "Programming", "Editorial EFG", (short) 250, "ENG", "Jhon Smith", LocalDate.of(2023,5,5), "987654321", true);
    Book book3 = new Book(3, "Testing with JUnit", "Programming", "Editorial ABC", (short) 280, "ESP", "Pepito Perez", LocalDate.of(2020,3,8), "654987234", true);

    Book book4 = new Book(4, "Programming with Java and Spring Boot", "Programming", "Editorial ABC", (short) 220, "ENG", "Jhon Smith", LocalDate.of(2022,4,3), "756347894", false);
    Book book5 = new Book(5, "Programming with Node and NextJs", "Programming", "Editorial XYZ", (short) 230, "ESP", "Pepito Perez", LocalDate.of(2021,6,6), "0987634733", false);


    @AfterEach
    public void setup(){

    }

    @Test
    public void getBooksByTitle_success() throws Exception{
        List<Book> books = List.of(book1,book2);
        String title = "Testing with Mockito";
        Mockito.when(bookRepository.findByTitle(title)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/books/titles/"+title)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(2)) )
                .andExpect( jsonPath("$[0].title",  Matchers.is(title)) );
    }

    @Test
    public void getBooksByTitle_noContent() throws Exception{
        List<Book> books = List.of();
        String title = "Testing with Mockito";
        Mockito.when(bookRepository.findByTitle(title)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/titles/"+title)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBooksByGender_success() throws Exception{
        List<Book> books = List.of(book1,book2);
        String gender = "Programming";
        Mockito.when(bookRepository.findByGender(gender)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/genders/"+gender)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(2)) )
                .andExpect( jsonPath("$[0].gender",  Matchers.is(gender)) );
    }

    @Test
    public void getBooksByGender_noContent() throws Exception{
        List<Book> books = List.of();
        String gender = "Programming";
        Mockito.when(bookRepository.findByGender(gender)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/genders/"+gender)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBooksByAuthor_success() throws Exception{
        List<Book> books = List.of(book1,book3);
        String author = "Pepito Perez";
        Mockito.when(bookRepository.findByAuthor(author)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/authors/"+author)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(2)) )
                .andExpect( jsonPath("$[0].author",  Matchers.is(author)) );
    }

    @Test
    public void getBooksByAuthor_noContent() throws Exception{
        List<Book> books = List.of();
        String author = "Pepito Perez";
        Mockito.when(bookRepository.findByAuthor(author)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/authors/"+author)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBookByIsbnCode_success() throws Exception{
        String isbnCode = "123456789";
        Mockito.when(bookRepository.findByIsbnCode(isbnCode)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/codes/"+isbnCode)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$.isbnCode",  Matchers.is(isbnCode)) );
    }

    @Test
    public void getBookByIsbnCode_notFound() throws Exception{
        String isbnCode = "123456789";
        Mockito.when(bookRepository.findByIsbnCode(isbnCode)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/codes/"+isbnCode)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBooksByLanguage_success() throws Exception{
        List<Book> books = List.of(book1,book3);
        String language = "ESP";
        Mockito.when(bookRepository.findByLanguage(language)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/languages/"+language)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(2)) )
                .andExpect( jsonPath("$[0].language",  Matchers.is(language)) );
    }

    @Test
    public void getBooksByLanguage_noContent() throws Exception{
        List<Book> books = List.of();
        String language = "Pepito Perez";
        Mockito.when(bookRepository.findByLanguage(language)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/languages/"+language)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeBookByIsbnCode_success() throws Exception{
        String isbnCode = "ESP";

        Mockito.when(bookRepository.existsByIsbnCode(isbnCode)).thenReturn(true);
        Mockito.when(bookRepository.findByIsbnCode(isbnCode)).thenReturn(book1);
        Mockito.when(bookRepository.save(book1)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/codes/"+isbnCode)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeBookByIsbnCode_notFound() throws Exception{
        String isbnCode = "ESP";

        Mockito.when(bookRepository.existsByIsbnCode(isbnCode)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/codes/"+isbnCode)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBooks_success() throws Exception{
        List<Book> books = List.of(book1,book2,book3,book4,book5);

        Mockito.when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(5)) );
    }

    @Test
    public void removeBookById_success() throws Exception{
        int bookId = 1;

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
        Mockito.when(bookRepository.save(book1)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/ids/"+bookId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeBookById_notFound() throws Exception{
        int bookId = 1;

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/ids/"+bookId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void registerBook_success() throws Exception{
        String isbnCode = "123456789";

        Book book = new Book();
        book.setTitle("Testing with Mockito");
        book.setGender("Programming");
        book.setEditorial("Editorial ABC");
        book.setPages((short) 254);
        book.setLanguage("ESP");
        book.setAuthor("Pepito Perez");
        book.setPublishedAt(LocalDate.of(2021,1,1));
        book.setIsbnCode("123456789");
        book.setActivated(true);

        Mockito.when(bookRepository.existsByIsbnCode(isbnCode)).thenReturn(false);
        Mockito.when(bookRepository.save(any())).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book))
                )
                .andExpect(status().isCreated())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$.isbnCode",  Matchers.is(isbnCode)) );
    }

    @Test
    public void registerBook_found() throws Exception{
        String isbnCode = "123456789";

        Mockito.when(bookRepository.existsByIsbnCode(isbnCode)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book1))
                )
                .andExpect(status().isFound());
    }

    @Test
    public void editBook_success() throws Exception{
        int bookId = 1;

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
        Mockito.when(bookRepository.save(any())).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book1))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void editBook_notFound() throws Exception{
        int bookId = 1;

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book1))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBooksByIsActivated_success() throws Exception{
        List<Book> books = List.of(book1,book2,book3);

        Mockito.when(bookRepository.findByActivated(true)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books?activated=ACTIVATED")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", notNullValue()) )
                .andExpect( jsonPath("$", Matchers.hasSize(3)) );
    }

    @Test
    public void getBooks_noContent() throws Exception{
        List<Book> books = List.of();
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

}