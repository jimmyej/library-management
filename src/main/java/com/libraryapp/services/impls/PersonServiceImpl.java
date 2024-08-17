package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.PersonRepository;
import com.libraryapp.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person registerPerson(PersonRequest person) {
        boolean existsByDocNumber = personRepository.existsByDocNumber(person.getDocNumber());

        if (!existsByDocNumber) {
            return personRepository.save(buildPersonRequest(person, 0));
        }

        return null;
    }

    private Person buildPersonRequest(PersonRequest people, int id) {

        Person newPeople = new Person();
        if (id > 0) {
            newPeople.setPersonId(id);
        }
        newPeople.setFirstName(people.getFirstName());
        newPeople.setLastName(people.getLastName());
        newPeople.setBirthdate(people.getBirthdate());
        newPeople.setDocType(people.getDocType());
        newPeople.setDocNumber(people.getDocNumber());
        newPeople.setAddress(people.getAddress());
        newPeople.setEmail(people.getEmail());
        newPeople.setPhoneNumber(people.getPhoneNumber());
        newPeople.setCreatedAt(people.getCreatedAt());
        newPeople.setCreatedBy(people.getCreatedBy());
        newPeople.setUpdatedAt(people.getUpdatedAt());
        newPeople.setUpdatedBy(people.getUpdatedBy());
        newPeople.setActivated(people.isActivated());

        return newPeople;
    }

    public Person getPersonByDocNumber(String docNumber) {
        boolean existsByDocNumber = personRepository.existsByDocNumber(docNumber);

        if (existsByDocNumber) {
            return personRepository.findByDocNumber(docNumber);
        }
        return null;
    }

    public List<Person> getPeopleByDocType(String docType) {

        return personRepository.findByDocType(docType);
    }

    public List<Person> getPeopleByActivated(String activated) {
        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return personRepository.findByActivated(status);
        } else {
            return (List<Person>) personRepository.findAll();
        }
    }

    public Person editPerson(PersonRequest personRequest) {

        boolean existsById = personRepository.existsById(personRequest.getPersonId());

        if (existsById) {
            Optional<Person> existingPerson = personRepository.findById(personRequest.getPersonId());
            if (existingPerson.isPresent()) {
                return personRepository.save(buildPersonRequest(personRequest, personRequest.getPersonId()));
            }
            return null;
        }
        return null;
    }

    public boolean removePersonById(int personId) {
        boolean isDeleted = false;
        try {
            boolean existsById = personRepository.existsById(personId);
            if (existsById) {
                Optional<Person> person = personRepository.findById(personId);
                if (person.isPresent()) {
                    person.get().setActivated(false);
                    personRepository.save(person.get());
                    isDeleted = true;
                }
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return isDeleted;
    }

    public boolean removePersonByDocNumber(String docNumber) {
        boolean isDeleted = false;
        try {
            boolean existsByDocNumber = personRepository.existsByDocNumber(docNumber);
            if (existsByDocNumber) {
                Person person = personRepository.findByDocNumber(docNumber);
                person.setActivated(false);
                personRepository.save(person);
                isDeleted = true;
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return isDeleted;
    }

}
