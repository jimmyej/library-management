package com.libraryapp.entities;

import java.time.LocalDate;

import javax.persistence.*;

import com.libraryapp.enums.CommonConstants;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table( name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;

    @Column(name = "customer_name", length = 100)
    private String customerName;
    @Column(name = "employee_name", length = 100)
    private String employeeName;
    @Column(name = "loan_date")
    private LocalDate loanDate;
    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status", length = 50)
    private CommonConstants loanStatus;
    
}
