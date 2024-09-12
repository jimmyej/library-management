package com.libraryapp.controllers;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.repositories.PersonRepository;
import com.libraryapp.services.impls.PersonServiceImpl;

@WebMvcTest(PersonController.class)
@Import(PersonServiceImpl.class)
@TestPropertySource(properties = {"spring.security.enabled=false"})
class PeopleControllersTests {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        PersonRepository personRepository;

        @Autowired
        ObjectMapper mapper;

        Person person1 = new Person(
                        1, "Juan", "Pérez",
                        LocalDate.of(1985, 5, 15),
                        "DNI", "12345678",
                        "123 Main Street",
                        "juan.perez@example.com",
                        "+1234567890",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                        "ADMIN-BOT",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                        "SUPERVISOR-BOT",
                        true);

        Person person2 = new Person(
                        2, "Maria", "Gonzalez",
                        LocalDate.of(1990, 7, 22),
                        "DNI", "87654321",
                        "456 Elm Street",
                        "maria.gonzalez@example.com",
                        "+0987654321",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                "ADMIN-BOT",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                "SUPERVISOR-BOT",
                        false);

        Person person3 = new Person(
                        3, "Carlos", "Fernandez",
                        LocalDate.of(1988, 11, 10),
                        "DNI", "34567890",
                        "789 Pine Street",
                        "carlos.fernandez@example.com",
                        "+1122334455",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                "ADMIN-BOT",
                        LocalDateTime.of(2024, 8, 12, 8, 30,50),
                "SUPERVISOR-BOT",
                        true);

        @Test
        void getPeopleByDocNumber_success() throws Exception {
                String docNumber = "12345678";
                Mockito.when(personRepository.existsByDocNumber(docNumber)).thenReturn(true);
                Mockito.when(personRepository.findByDocNumber(docNumber)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people/docNumber/" + docNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$.docNumber", Matchers.is(docNumber)));
        }

        @Test
        void getPeopleByDocNumber_notFound() throws Exception {

                String docNumber = "12345678";
                Mockito.when(personRepository.findByDocNumber(docNumber)).thenReturn(null);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people/docNumber/" + docNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getPeopleByDocType_success() throws Exception {
                List<Person> peoples = List.of(person1, person3);
                String docType = "DNI";
                Mockito.when(personRepository.findByDocType(docType)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people/docTypes/" + docType)
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
                Mockito.when(personRepository.findByDocType(docType)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people/docTypes/" + docType)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void getPeoplesByIsActivated_success() throws Exception {

                List<Person> peoples = List.of(person1, person2, person3);

                Mockito.when(personRepository.findByActivated(true)).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people?activated=ACTIVATED")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", notNullValue()))
                                .andExpect(jsonPath("$", Matchers.hasSize(3)));
        }

        @Test
        void getPeoples_noContact() throws Exception {

                List<Person> peoples = List.of();
                Mockito.when(personRepository.findAll()).thenReturn(peoples);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/people")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        private PersonRequest buildPersonRequest(){
                PersonRequest person = new PersonRequest();
                person.setFirstName("Juan");
                person.setLastName("Pérez");
                person.setBirthdate(LocalDate.of(1985, 5, 15));
                person.setDocType("DNI");
                person.setDocNumber("12345678");
                person.setAddress("123 Main Street");
                person.setEmail("juan.perez@example.com");
                person.setPhoneNumber("+1234567890");
                person.setCreatedAt(LocalDateTime.of(2024, 8, 12, 8, 30,50));
                person.setCreatedBy("ADMIN-BOT");
                person.setUpdatedAt(LocalDateTime.of(2024, 8, 12, 8, 30,50));
                person.setUpdatedBy("SUPERVISOR-BOT");
                person.setActivated(true);
                return person;
        }

        @Test
        void registerPeople_success() throws Exception {
                String docNumber = "12345678";

                PersonRequest person = buildPersonRequest();

                        Mockito.when(personRepository.existsByDocNumber(docNumber)).thenReturn(false);
                Mockito.when(personRepository.save(any())).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/people")
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

                Mockito.when(personRepository.existsByDocNumber(docNumber)).thenReturn(true);

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1)))
                                .andExpect(status().isFound());
        }

        @Test
        void editPeople_success() throws Exception {
                int personId = 1;

                Mockito.when(personRepository.existsById(personId)).thenReturn(true);
                Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person1));
                Mockito.when(personRepository.save(any())).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1))).andExpect(status().isOk());
        }

        @Test
        void editPeople_notFound() throws Exception {
                int personId = 1;

                Mockito.when(personRepository.existsById(personId)).thenReturn(false);

                mockMvc.perform(MockMvcRequestBuilders
                                .put("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(person1))).andExpect(status().isNotFound());

        }

        @Test
        void removePeopleById_success() throws Exception {
                int personId = 1;

                Mockito.when(personRepository.existsById(personId)).thenReturn(true);
                Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person1));
                Mockito.when(personRepository.save(person1)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

        }

        @Test
        void removePeopleById_notPresent() throws Exception {
                int personId = 1;

                Mockito.when(personRepository.existsById(personId)).thenReturn(true);
                Mockito.when(personRepository.findById(personId)).thenReturn(Optional.empty());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/ids/" + personId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleById_exception() throws Exception {
                int personId = 1;

                Mockito.when(personRepository.findById(personId)).thenThrow(new NullPointerException());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/ids/" + personId)
                                       .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleByDocNumber_success() throws Exception {
                String peopleDocNumber = "12345678";

                Mockito.when(personRepository.existsByDocNumber(peopleDocNumber)).thenReturn(true);
                Mockito.when(personRepository.findByDocNumber(peopleDocNumber)).thenReturn(person1);
                Mockito.when(personRepository.save(person1)).thenReturn(person1);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void removePeopleByDocNumber_notFound() throws Exception {
                String peopleDocNumber = "12345678";

                Mockito.when(personRepository.existsByDocNumber(peopleDocNumber)).thenReturn(false);

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void removePeopleByDocNumber_exception() throws Exception {
                String peopleDocNumber = "12345678";
                Mockito.when(personRepository.existsByDocNumber(peopleDocNumber)).thenThrow(new NullPointerException());

                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/api/v1/people/docNumbers/" + peopleDocNumber)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }
}
