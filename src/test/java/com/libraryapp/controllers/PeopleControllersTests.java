package com.libraryapp.controllers;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.repositories.PeopleRepository;
import com.libraryapp.services.impls.PeopleServiceImpl;

@WebMvcTest(PeopleController.class)
@Import(PeopleServiceImpl.class)
class PeopleControllersTests {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        PeopleRepository peopleRepository;

        @Autowired
        ObjectMapper mapper;

        Person person1 = new Person(
                        1, "Juan", "Pérez",
                        LocalDate.of(1985, 5, 15),
                        "DNI", "12345678",
                        "123 Main Street",
                        "juan.perez@example.com",
                        "+1234567890",
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        true);

        Person person2 = new Person(
                        2, "Maria", "Gonzalez",
                        LocalDate.of(1990, 7, 22),
                        "DNI", "87654321",
                        "456 Elm Street",
                        "maria.gonzalez@example.com",
                        "+0987654321",
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        false);

        Person person3 = new Person(
                        3, "Carlos", "Fernandez",
                        LocalDate.of(1988, 11, 10),
                        "DNI", "34567890",
                        "789 Pine Street",
                        "carlos.fernandez@example.com",
                        "+1122334455",
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        true);

        Person person4 = new Person(
                        4, "Laura", "Martinez",
                        LocalDate.of(1995, 2, 28),
                        "DNI", "45678901",
                        "101 Maple Street",
                        "laura.martinez@example.com",
                        "+2233445566",
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        LocalDate.of(2024, 8, 12),
                        false);

        @AfterEach
        void setup() {

        }

        @Test
        void getPeopleByDocNumber_success() throws Exception {
                String docNumber = "12345678";
                Mockito.when(peopleRepository.existsByDocNumber(docNumber)).thenReturn(true);
                Mockito.when(peopleRepository.findByDocNumber(docNumber)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples/docNumber/" + docNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$.docNumber", Matchers.is(docNumber)));
        }

        @Test
        void getPeopleByDocNumber_notFound() throws Exception {

                String docNumber = "12345678";
                Mockito.when(peopleRepository.findByDocNumber(docNumber)).thenReturn(null);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples/docNumber/" + docNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getPeopleByDocType_success() throws Exception {
                List<Person> peoples = List.of(person1, person3);
                String docType = "DNI";
                Mockito.when(peopleRepository.findByDocType(docType)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples/docTypes/" + docType)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                                .andExpect(jsonPath("$[0].docType", Matchers.is(docType)));
        }

        @Test
        void getPeopleByDocType_noContent() throws Exception {

                List<Person> peoples = List.of();
                String docType = "DNI";
                Mockito.when(peopleRepository.findByDocType(docType)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples/docTypes/" + docType)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void getPeoplesByIsActivated_success() throws Exception {

                List<Person> peoples = List.of(person1, person2, person3);

                Mockito.when(peopleRepository.findByActivated(true)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples?activated=ACTIVATED")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$", Matchers.hasSize(3)));
        }

        @Test
        void getPeoples_noContact() throws Exception {

                List<Person> peoples = List.of();
                Mockito.when(peopleRepository.findAll()).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/peoples")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void registerPeople_success() throws Exception {
                String docNumber = "12345678";

                PersonRequest person = new PersonRequest();
                person.setFirstName("Juan");
                person.setLastName("Pérez");
                person.setBirthdate(LocalDate.of(1985, 5, 15));
                person.setDocType("DNI");
                person.setDocNumber("12345678");
                person.setAddress("123 Main Street");
                person.setEmail("juan.perez@example.com");
                person.setPhoneNumber("+1234567890");
                person.setCreatedAt(LocalDate.of(2024, 8, 12));
                person.setCreatedBy(LocalDate.of(2024, 8, 12));
                person.setUpdatedAt(LocalDate.of(2024, 8, 12));
                person.setUpdatedBy(LocalDate.of(2024, 8, 12));
                person.setActivated(true);

                Mockito.when(peopleRepository.existsByDocNumber(docNumber)).thenReturn(false);
                Mockito.when(peopleRepository.save(any())).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/peoples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$.docNumber", Matchers.is(docNumber)));

        }

        @Test
        void registerPeople_found() throws Exception {
                String docNumber = "12345678";

                Mockito.when(peopleRepository.existsByDocNumber(docNumber)).thenReturn(true);

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/peoples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1)))
                                .andExpect(status().isFound());
        }

        @Test
        void editPeople_success() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenReturn(true);
                Mockito.when(peopleRepository.findById(personId)).thenReturn(Optional.of(person1));
                Mockito.when(peopleRepository.save(any())).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/peoples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1))).andExpect(status().isOk());
        }

        @Test
        void editPeople_notPresent() throws Exception {

                int personId = 1;
                Mockito.when(peopleRepository.existsById(personId)).thenReturn(true);
                Mockito.when(peopleRepository.findById(personId)).thenReturn(Optional.empty());

                mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/peoples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1))).andExpect(status().isNotFound());
        }

        @Test
        void editPeople_notFound() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenReturn(false);

                mockMvc.perform(MockMvcRequestBuilders
                                .put("/api/v1/peoples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1))).andExpect(status().isNotFound());

        }

        @Test
        void removePeopleById_success() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenReturn(true);
                Mockito.when(peopleRepository.findById(personId)).thenReturn(Optional.of(person1));
                Mockito.when(peopleRepository.save(person1)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

        }

        @Test
        void removePeopleById_notPresent() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenReturn(true);
                Mockito.when(peopleRepository.findById(personId)).thenReturn(Optional.empty());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleById_notFound() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenReturn(false);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleById_exception() throws Exception {
                int personId = 1;

                Mockito.when(peopleRepository.existsById(personId)).thenThrow(new NullPointerException());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleByDocNumber_success() throws Exception {
                String peopleDocNumber = "12345678";

                Mockito.when(peopleRepository.existsByDocNumber(peopleDocNumber)).thenReturn(true);
                Mockito.when(peopleRepository.findByDocNumber(peopleDocNumber)).thenReturn(person1);
                Mockito.when(peopleRepository.save(person1)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void removePeopleByDocNumber_notFound() throws Exception {
                String peopleDocNumber = "12345678";

                Mockito.when(peopleRepository.existsByDocNumber(peopleDocNumber)).thenReturn(false);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleByDocNumber_exception() throws Exception {
                String peopleDocNumber = "12345678";

                Mockito.when(peopleRepository.existsByDocNumber(peopleDocNumber)).thenThrow(new NullPointerException());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/peoples/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }
}
