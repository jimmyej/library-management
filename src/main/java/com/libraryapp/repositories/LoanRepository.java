package com.libraryapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Loan;
import com.libraryapp.enums.LoanConstants;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer>{

    List<Loan> findByCustomerName(String customerName);
    List<Loan> findByEmployeeName(String employeeName);
    List<Loan> findByLoanStatus(LoanConstants loanStatus);

}
