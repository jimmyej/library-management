package com.libraryapp.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.libraryapp.dtos.requests.LoanRequest;
import com.libraryapp.entities.Loan;
import com.libraryapp.services.LoanService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    LoanService loanService;
    LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @GetMapping("/customers/{customerName}")
    ResponseEntity<List<Loan>> getLoansByCustomerName(@PathVariable String customerName) {
       List<Loan> loans = loanService.getLoansByCustomerName(customerName);
       if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
       }
       return ResponseEntity.ok(loans);
    }
    
    @GetMapping("/employees/{employeeName}")
    ResponseEntity<List<Loan>> getLoansByEmployeeName(@PathVariable String employeeName) {
        List<Loan> loans = loanService.getLoansByEmployeeName(employeeName);
        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }
    
    @GetMapping("")
    ResponseEntity<List<Loan>> getLoansByStatus(@RequestParam(required = false) String loanStatus) {
        List<Loan> loans = loanService.getLoansByStatus(loanStatus);
        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }

    @PostMapping("")
    ResponseEntity<Loan> registerLoan(@RequestBody LoanRequest loan) {
        Loan newLoan = loanService.registerLoan(loan);
        if (newLoan != null) {
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    ResponseEntity<Loan> editLoan(@RequestBody LoanRequest loan) {
        Loan editLoan = loanService.editLoan(loan);
        if (editLoan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editLoan);
    }
    
    @DeleteMapping("/ids/{loanId}")
    ResponseEntity<Boolean> removeLoanById(@PathVariable int loanId){
        boolean removed = loanService.removeLoanById(loanId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
