package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.services.PeopleService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/peoples")
public class PeopleController {

    PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/docNumber/{docNumber}")
    ResponseEntity<Person> getPeopleByDocNumber(@PathVariable String docNumber) {
        Person peoples = peopleService.getPeopleByDocNumber(docNumber);
        if (peoples == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @GetMapping("/docTypes/{docType}")
    ResponseEntity<List<Person>> getPeopleByDocType(@PathVariable String docType) {
        List<Person> peoples = peopleService.getPeopleByDocType(docType);
        if (peoples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @GetMapping("")
    ResponseEntity<List<Person>> getPeoplesByIsActivated(@RequestParam(required = false) String activated) {
        List<Person> peoples = peopleService.getPeoplesByActivated(activated);
        if (peoples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @PostMapping("")
    ResponseEntity<Person> registerPeople(@RequestBody PersonRequest people) {
        Person newPeople = peopleService.registerPeople(people);

        if (newPeople != null) {
            return new ResponseEntity<>(newPeople, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    ResponseEntity<Person> editPeople(@RequestBody PersonRequest people) {

        Person newPeople = peopleService.editPeople(people);
        if (newPeople == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newPeople);
    }

    @DeleteMapping("/ids/{peopleId}")
    ResponseEntity<Boolean> removePeopleById(@PathVariable int peopleId) {
        boolean removed = peopleService.removePeopleById(peopleId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/docNumbers/{peopleDocNumber}")
    ResponseEntity<Boolean> removePeopleByDocNumber(@PathVariable String peopleDocNumber) {

        boolean removed = peopleService.removePeopleByDocNumber(peopleDocNumber);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}