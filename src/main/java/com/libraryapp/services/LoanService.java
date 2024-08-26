package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.LoanRequest;
import com.libraryapp.entities.Loan;

public interface LoanService {
    List<Loan> getLoansByCustomerName(String customerName);
    List<Loan> getLoansByEmployeeName(String employeeName);

    List<Loan> getLoansByStatus(String status);
    Loan registerLoan(LoanRequest loan);
    Loan editLoan(LoanRequest loan);
    boolean removeLoanById(int loanId);
}
