package com.libraryapp.dtos.requests;

import java.time.LocalDate;

import com.libraryapp.enums.CommonConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoanRequest {
    private int loanId;
    private String customerName;
    private String employeeName;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private CommonConstants loanStatus;
}
