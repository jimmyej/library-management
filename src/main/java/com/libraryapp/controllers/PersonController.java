package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.services.PersonService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/v1/people")
public class PersonController {

    PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/docNumber/{docNumber}")
    ResponseEntity<Person> getPersonByDocNumber(@PathVariable String docNumber) {
        Person person = personService.getPersonByDocNumber(docNumber);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person);
    }

    @GetMapping("/docTypes/{docType}")
    ResponseEntity<List<Person>> getPeopleByDocType(@PathVariable String docType) {
        List<Person> people = personService.getPeopleByDocType(docType);
        if (people.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(people);
    }

    @GetMapping("")
    ResponseEntity<List<Person>> getPeoplesByIsActivated(@RequestParam(required = false) String activated) {
        List<Person> people = personService.getPeopleByActivated(activated);
        if (people.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(people);
    }

    @PostMapping("")
    ResponseEntity<Person> registerPerson(@RequestBody PersonRequest person) {
        Person newPerson = personService.registerPerson(person);

        if (newPerson != null) {
            return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    ResponseEntity<Person> editPerson(@RequestBody PersonRequest person) {

        Person newPerson = personService.editPerson(person);
        if (newPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newPerson);
    }

    @DeleteMapping("/ids/{personId}")
    ResponseEntity<Boolean> removePeopleById(@PathVariable int personId) {
        boolean removed = personService.removePersonById(personId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/docNumbers/{personDocNumber}")
    ResponseEntity<Boolean> removePersonByDocNumber(@PathVariable String personDocNumber) {

        boolean removed = personService.removePersonByDocNumber(personDocNumber);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}