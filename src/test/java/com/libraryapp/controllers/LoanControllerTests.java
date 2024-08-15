package com.libraryapp.controllers;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.dtos.requests.LoanRequest;
import com.libraryapp.entities.Loan;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.LoanRepository;
import com.libraryapp.services.impls.LoanServiceImpl;

@WebMvcTest(LoanController.class)
@Import(LoanServiceImpl.class)
class LoanControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoanRepository loanRepository;

    @Autowired
    ObjectMapper mapper;

    Loan loan1 = new Loan(1,"Jose Perez", "Carlos Ramirez", 
            LocalDate.of(2024, 7, 25), LocalDate.of(2024, 7, 30), CommonConstants.LOANED);
    Loan loan2 = new Loan(2,"Pedro Sanchez", "Carlos Ramirez", 
            LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 8), CommonConstants.RESERVED);
    Loan loan3 = new Loan(3,"Ramona Castilla", "Jhon Smith", 
            LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 10), CommonConstants.LOANED);
    Loan loan4 = new Loan(3,"Ramona Castilla", "Jhon Smith", 
            LocalDate.of(2024, 7, 20), LocalDate.of(2024, 8, 1), CommonConstants.FINISHED);

    Loan loan5 = new Loan(4,"Pedro Sanchez", "Carlos Ramirez", 
            LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 1), CommonConstants.INACTIVATED);
    Loan loan6 = new Loan(5,"Ramona Castilla", "Jhon ", 
            LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 1), CommonConstants.INACTIVATED);

    @AfterEach
    void setup() { 
    
    }

    @Test
    void getLoansByCustomerName_success() throws Exception {
        List<Loan> loans = List.of(loan3, loan4);
        String customerName = "Ramona Castilla";
        Mockito.when(loanRepository.findByCustomerName(customerName)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans/customers/" + customerName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].customerName", Matchers.is(customerName)));
    }

    @Test
    void getLoansByCustomerName_noContent() throws Exception {
        List<Loan> loans = List.of();
        String customerName = "Ramona Castilla";
        Mockito.when(loanRepository.findByCustomerName(customerName)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans/customers/" + customerName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLoansByEmployeeName_success() throws Exception {
        List<Loan> loans = List.of(loan1, loan2);
        String employeeName = "Carlos Ramirez";
        Mockito.when(loanRepository.findByEmployeeName(employeeName)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans/employees/" + employeeName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].employeeName", Matchers.is(employeeName)));
    }

    @Test
    void getLoansByEmployeeName_noContent() throws Exception {
        List<Loan> loans = List.of();
        String employeeName = "Carlos Ramirez";
        Mockito.when(loanRepository.findByCustomerName(employeeName)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans/employees/" + employeeName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLoansByStatus_success() throws Exception {
        List<Loan> loans = List.of(loan1, loan2, loan3, loan4, loan5, loan6);
        Mockito.when(loanRepository.findByLoanStatus(CommonConstants.ACTIVATED)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans?loanStatus=ACTIVATED")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(6)));
    }

    @Test
    void getLoansByStatus_noContent() throws Exception {
        List<Loan> loans = List.of();
        Mockito.when(loanRepository.findAll()).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLoans_success() throws Exception {
        List<Loan> loans = List.of(loan1, loan2, loan3, loan4, loan5, loan6);
        Mockito.when(loanRepository.findAll()).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(6)));
    }

    @Test
    void registerLoan_success() throws Exception {
        LoanRequest loan = new LoanRequest();
        loan.setCustomerName("Francis McBucket");
        loan.setEmployeeName("Carlos Ramirez");
        loan.setLoanDate(LocalDate.of(2024, 8, 5));
        loan.setReturnDate(LocalDate.of(2024, 8, 15));
        loan.setLoanStatus(CommonConstants.RESERVED);
        
        Mockito.when(loanRepository.save(any())).thenReturn(loan1);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loan)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()));
    }

    @Test
    void registerLoan_found() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loan1)))
                .andExpect(status().isFound());

    }

    @Test
    void editLoan_success() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(true);
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan1));
        Mockito.when(loanRepository.save(any())).thenReturn(loan1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loan1)))
                .andExpect(status().isOk());
    }

    @Test
    void editLoan_notPresent() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(true);
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loan1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void editLoan_notFound() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loan1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeLoanByLoanId_success() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(true);
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan1));
        Mockito.when(loanRepository.save(loan1)).thenReturn(loan1);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/loans/ids/" + loanId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeLoanByLoanId_notPresent() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(true);
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/loans/ids/" + loanId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void removeLoanByLoanId_notFound() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/loans/ids/" + loanId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void removeLoanById_exception() throws Exception {
        int loanId = 1;
        Mockito.when(loanRepository.existsById(loanId)).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/loans/ids/" + loanId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
