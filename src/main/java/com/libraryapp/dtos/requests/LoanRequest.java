package com.libraryapp.dtos.requests;

import java.time.LocalDateTime;

import com.libraryapp.enums.LoanConstants;

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
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private LoanConstants loanStatus;
}
