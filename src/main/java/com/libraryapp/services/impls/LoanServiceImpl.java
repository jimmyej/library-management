package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.LoanRequest;
import com.libraryapp.entities.Loan;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.LoanRepository;
import com.libraryapp.services.LoanService;

@Service
public class LoanServiceImpl implements LoanService{

    LoanRepository loanRepository;

    @Autowired
    LoanServiceImpl(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    public List<Loan> getLoansByCustomerName(String customerName) {
        return loanRepository.findByCustomerName(customerName);
    }

    public List<Loan> getLoansByEmployeeName(String employeeName) {
        return loanRepository.findByEmployeeName(employeeName);
    }

    public List<Loan> getLoansByStatus(String loanStatus) {
        if(loanStatus != null){
            CommonConstants status = CommonConstants.valueOf(loanStatus.toUpperCase());
            return loanRepository.findByLoanStatus(status);
        } else {
            return (List<Loan>) loanRepository.findAll();
        }
    }

    public Loan registerLoan(LoanRequest loan) {
        Optional<Loan> newLoan = loanRepository.findById(loan.getLoanId());
        if (!newLoan.isPresent()) {
            return loanRepository.save(buildLoanRequest(loan, 0));
        }
        return null;
    }

    public Loan editLoan(LoanRequest loan) {
        Optional<Loan> existingLoan = loanRepository.findById(loan.getLoanId());
        if(existingLoan.isPresent()) {
            return loanRepository.save(buildLoanRequest(loan, loan.getLoanId()));
        }
        return null;
    }

    public boolean removeLoanById(int loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        if(loan.isPresent()) {
            loan.get().setLoanStatus(CommonConstants.INACTIVATED);
            loanRepository.save(loan.get());
            return true;
        }
        return false;
    }

    private Loan buildLoanRequest(LoanRequest loan, int id) {
        Loan newLoan = new Loan();
        if(id > 0) {
            newLoan.setLoanId(id);
        }
        newLoan.setCustomerName(loan.getCustomerName());
        newLoan.setEmployeeName(loan.getEmployeeName());
        newLoan.setLoanDate(loan.getLoanDate());
        newLoan.setReturnDate(loan.getReturnDate());
        newLoan.setLoanStatus(loan.getLoanStatus());
        return newLoan;
    }

}
