package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.PeopleRequest;
import com.libraryapp.entities.People;

public interface PeopleService {
    People registerPeople(PeopleRequest people);

    People getPeopleByDocNumber(String docNumber);

    List<People> getPeopleByDocType(String docType);

    List<People> getPeoplesByActivated(String activated);

    People editPeople(PeopleRequest peopleRequest);

    boolean removePeopleById(int peopleId);

    boolean removePeopleByDocNumber(String docNumber);

}
