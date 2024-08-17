package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;

public interface PersonService {
    Person registerPerson(PersonRequest person);

    Person getPersonByDocNumber(String docNumber);

    List<Person> getPeopleByDocType(String docType);

    List<Person> getPeopleByActivated(String activated);

    Person editPerson(PersonRequest personRequest);

    boolean removePersonById(int personId);

    boolean removePersonByDocNumber(String docNumber);

}
