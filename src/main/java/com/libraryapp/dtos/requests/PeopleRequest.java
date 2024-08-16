package com.libraryapp.dtos.requests;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PeopleRequest {

    private int peopleId;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private String docType;

    private String docNumber;

    private String address;

    private String email;

    private String phoneNumber;

    private LocalDate createdAt;

    private LocalDate createdBy;

    private LocalDate updatedAt;

    private LocalDate updatedBy;

    private boolean activated;

}
