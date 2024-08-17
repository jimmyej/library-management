package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;

public interface PeopleService {
    Person registerPeople(PersonRequest people);

    Person getPeopleByDocNumber(String docNumber);

    List<Person> getPeopleByDocType(String docType);

    List<Person> getPeoplesByActivated(String activated);

    Person editPeople(PersonRequest peopleRequest);

    boolean removePeopleById(int peopleId);

    boolean removePeopleByDocNumber(String docNumber);

}
