package com.libraryapp.entities;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "peoples")
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int peopleId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    private LocalDate birthdate;

    @Column(name = "doc_type", length = 50)
    private String docType;

    @Column(name = "doc_number", unique = true, length = 40)
    private String docNumber;

    @Column(length = 100)
    private String address;

    @Column(length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "created_by")
    private LocalDate createdBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "updated_by")
    private LocalDate updatedBy;

    @Column(name = "is_activated")
    private boolean activated;

}
