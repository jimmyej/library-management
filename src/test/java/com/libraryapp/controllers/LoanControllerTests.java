package com.libraryapp.controllers;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.libraryapp.securities.AuthEntryPointJwt;
import com.libraryapp.securities.JwtUtils;
import com.libraryapp.securities.WebSecurityConfig;
import com.libraryapp.services.impls.UserDetailsServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.dtos.requests.LoanRequest;
import com.libraryapp.entities.Loan;
import com.libraryapp.enums.LoanConstants;
import com.libraryapp.repositories.LoanRepository;
import com.libraryapp.services.impls.LoanServiceImpl;

@SuppressWarnings("unused")
@ExtendWith(SpringExtension.class)
@WebMvcTest(LoanController.class)
@ActiveProfiles("test")
@Import({LoanServiceImpl.class, WebSecurityConfig.class, JwtUtils.class})
@WithMockUser(username = "user", authorities={"ROLE_USER", "ROLE_ADMIN"})
class LoanControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoanRepository loanRepository;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    JwtUtils jwtUtils;

    Loan loan1 = new Loan(1,"Jose Perez", "Carlos Ramirez", 
            LocalDateTime.of(2024, 7, 25, 10,45,20), LocalDateTime.of(2024, 7, 30, 10,45,20), LoanConstants.LOANED);
    Loan loan2 = new Loan(2,"Pedro Sanchez", "Carlos Ramirez",
            LocalDateTime.of(2024, 8, 1, 10,45,20), LocalDateTime.of(2024, 8, 8, 10,45,20), LoanConstants.RESERVED);
    Loan loan3 = new Loan(3,"Ramona Castilla", "Jhon Smith",
            LocalDateTime.of(2024, 8, 1, 10,45,20), LocalDateTime.of(2024, 8, 10, 10,45,20), LoanConstants.LOANED);
    Loan loan4 = new Loan(3,"Ramona Castilla", "Jhon Smith",
            LocalDateTime.of(2024, 7, 20, 10,45,20), LocalDateTime.of(2024, 8, 1, 10,45,20), LoanConstants.FINISHED);

    Loan loan5 = new Loan(4,"Pedro Sanchez", "Carlos Ramirez",
            LocalDateTime.of(2024, 8, 1, 10,45,20), LocalDateTime.of(2024, 8, 1, 10,45,20), LoanConstants.CANCELLED);
    Loan loan6 = new Loan(5,"Ramona Castilla", "Jhon ",
            LocalDateTime.of(2024, 8, 1, 10,45,20), LocalDateTime.of(2024, 8, 1, 10,45,20), LoanConstants.CANCELLED);

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
        Mockito.when(loanRepository.findByLoanStatus(LoanConstants.LOANED)).thenReturn(loans);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/loans?loanStatus=LOANED")
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
        loan.setLoanDate(LocalDateTime.of(2024, 8, 5, 10,45,20));
        loan.setReturnDate(LocalDateTime.of(2024, 8, 15, 10,45,20));
        loan.setLoanStatus(LoanConstants.RESERVED);
        
        Mockito.when(loanRepository.findById(0)).thenReturn(Optional.empty());
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
        
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan1));

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
        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

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

        Mockito.when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/loans/ids/" + loanId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
