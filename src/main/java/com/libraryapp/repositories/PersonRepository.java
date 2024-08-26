package com.libraryapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findByDocNumber(String docNumber);

    List<Person> findByDocType(String docType);

    List<Person> findByActivated(boolean activated);

    boolean existsByDocNumber(String docNumber);

}
