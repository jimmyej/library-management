package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.PersonRequest;
import com.libraryapp.entities.Person;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.PeopleRepository;
import com.libraryapp.services.PeopleService;

@Service
public class PeopleServiceImpl implements PeopleService {

    PeopleRepository peopleRepository;

    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person registerPeople(PersonRequest person) {
        boolean existsByDocNumber = peopleRepository.existsByDocNumber(person.getDocNumber());

        if (!existsByDocNumber) {
            return peopleRepository.save(builPeopleRequest(person, 0));
        }

        return null;
    }

    private Person builPeopleRequest(PersonRequest person, int id) {

        Person newPerson = new Person();
        if (id > 0) {
            newPerson.setPeopleId(id);
        }
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());
        newPerson.setBirthdate(person.getBirthdate());
        newPerson.setDocType(person.getDocType());
        newPerson.setDocNumber(person.getDocNumber());
        newPerson.setAddress(person.getAddress());
        newPerson.setEmail(person.getEmail());
        newPerson.setPhoneNumber(person.getPhoneNumber());
        newPerson.setCreatedAt(person.getCreatedAt());
        newPerson.setCreatedBy(person.getCreatedBy());
        newPerson.setUpdatedAt(person.getUpdatedAt());
        newPerson.setUpdatedBy(person.getUpdatedBy());
        newPerson.setActivated(person.isActivated());

        return newPerson;
    }

    public Person getPeopleByDocNumber(String docNumber) {
        boolean existsByDocNumber = peopleRepository.existsByDocNumber(docNumber);

        if (existsByDocNumber) {
            return peopleRepository.findByDocNumber(docNumber);
        }
        return null;
    }

    public List<Person> getPeopleByDocType(String docType) {

        return peopleRepository.findByDocType(docType);
    }

    public List<Person> getPeoplesByActivated(String activated) {
        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return peopleRepository.findByActivated(status);
        } else {
            return (List<Person>) peopleRepository.findAll();
        }
    }

    public Person editPeople(PersonRequest person) {

        boolean existsById = peopleRepository.existsById(person.getPeopleId());

        if (existsById) {
            Optional<Person> existingPeople = peopleRepository.findById(person.getPeopleId());
            if (existingPeople.isPresent()) {
                return peopleRepository.save(builPeopleRequest(person, person.getPeopleId()));
            }
            return null;
        }
        return null;
    }

    public boolean removePeopleById(int peopleId) {
        boolean isDeleted = false;
        try {
            boolean existsById = peopleRepository.existsById(peopleId);
            if (existsById) {
                Optional<Person> person = peopleRepository.findById(peopleId);
                if (person.isPresent()) {
                    person.get().setActivated(false);
                    peopleRepository.save(person.get());
                    isDeleted = true;
                }
            }
        } catch (Exception e) {
            e.getCause();
        }
        return isDeleted;
    }

    public boolean removePeopleByDocNumber(String docNumber) {
        boolean isDeleted = false;
        try {
            boolean existsByDocNumber = peopleRepository.existsByDocNumber(docNumber);
            if (existsByDocNumber) {
                Person person = peopleRepository.findByDocNumber(docNumber);
                person.setActivated(false);
                peopleRepository.save(person);
                isDeleted = true;
            }
        } catch (Exception e) {
            e.getCause();
        }
        return isDeleted;
    }

}
