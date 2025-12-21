package com.greentrack.backend.controller;

import com.greentrack.backend.dto.LoanDTO;
import com.greentrack.backend.dto.LoanRequestDTO;
import com.greentrack.backend.entity.Loan;
import com.greentrack.backend.service.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @Test
    void createLoan_ShouldReturn200() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setEquipmentId(1L);
        request.setUserId(1L);

        // simular entidad que devuelve el servicio
        Loan mockLoan = new Loan();
        mockLoan.setId(50L);
        mockLoan.setStatus(Loan.LoanStatus.ACTIVO);

        // configurar mock
        when(loanService.create(any(LoanRequestDTO.class))).thenReturn(mockLoan);

        // ejecucion
        ResponseEntity<LoanDTO> response = loanController.create(request);

        // verificacion
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(mockLoan.getId(), response.getBody().getId());
        assertEquals(mockLoan.getStatus().name(), response.getBody().getStatus());
    }
}