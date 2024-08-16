package com.libraryapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.People;

@Repository
public interface PeopleRepository extends CrudRepository<People, Integer> {

    People findByDocNumber(String docNumber);

    List<People> findByDocType(String docType);

    List<People> findByActivated(boolean activated);

    boolean deleteByDocNumber(String docNumber);

    boolean existsByDocNumber(String docNumber);

}
