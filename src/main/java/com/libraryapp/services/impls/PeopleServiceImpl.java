package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.PeopleRequest;
import com.libraryapp.entities.People;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.PeopleRepository;
import com.libraryapp.services.PeopleService;

@Service
public class PeopleServiceImpl implements PeopleService {

    PeopleRepository peopleRepository;

    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public People registerPeople(PeopleRequest people) {
        boolean existsByDocNumber = peopleRepository.existsByDocNumber(people.getDocNumber());

        if (!existsByDocNumber) {
            return peopleRepository.save(builPeopleRequest(people, 0));
        }

        return null;
    }

    private People builPeopleRequest(PeopleRequest people, int id) {

        People newPeople = new People();
        if (id > 0) {
            newPeople.setPeopleId(id);
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

    public People getPeopleByDocNumber(String docNumber) {
        boolean existsByDocNumber = peopleRepository.existsByDocNumber(docNumber);

        if (existsByDocNumber) {
            return peopleRepository.findByDocNumber(docNumber);
        }
        return null;
    }

    public List<People> getPeopleByDocType(String docType) {

        return peopleRepository.findByDocType(docType);
    }

    public List<People> getPeoplesByActivated(String activated) {
        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return peopleRepository.findByActivated(status);
        } else {
            return (List<People>) peopleRepository.findAll();
        }
    }

    public People editPeople(PeopleRequest people) {

        boolean existsById = peopleRepository.existsById(people.getPeopleId());

        if (existsById) {
            Optional<People> existingPeople = peopleRepository.findById(people.getPeopleId());
            if (existingPeople.isPresent()) {
                return peopleRepository.save(builPeopleRequest(people, people.getPeopleId()));
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
                Optional<People> people = peopleRepository.findById(peopleId);
                if (people.isPresent()) {
                    people.get().setActivated(false);
                    peopleRepository.save(people.get());
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
                People people = peopleRepository.findByDocNumber(docNumber);
                people.setActivated(false);
                peopleRepository.save(people);
                isDeleted = true;
            }
        } catch (Exception e) {
            e.getCause();
        }
        return isDeleted;
    }

}
