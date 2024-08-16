package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.PeopleRequest;
import com.libraryapp.entities.People;
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
    ResponseEntity<People> getPeopleByDocNumber(@PathVariable String docNumber) {
        People peoples = peopleService.getPeopleByDocNumber(docNumber);
        if (peoples == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @GetMapping("/docTypes/{docType}")
    ResponseEntity<List<People>> getPeopleByDocType(@PathVariable String docType) {
        List<People> peoples = peopleService.getPeopleByDocType(docType);
        if (peoples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @GetMapping("")
    ResponseEntity<List<People>> getPeoplesByIsActivated(@RequestParam(required = false) String activated) {
        List<People> peoples = peopleService.getPeoplesByActivated(activated);
        if (peoples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peoples);
    }

    @PostMapping("")
    ResponseEntity<People> registerPeople(@RequestBody PeopleRequest people) {
        People newPeople = peopleService.registerPeople(people);

        if (newPeople != null) {
            return new ResponseEntity<>(newPeople, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    ResponseEntity<People> editPeople(@RequestBody PeopleRequest people) {

        People newPeople = peopleService.editPeople(people);
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