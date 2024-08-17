package com.libraryapp.dtos.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonRequest {

    private int peopleId;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private String docType;

    private String docNumber;

    private String address;

    private String email;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private boolean activated;

}
