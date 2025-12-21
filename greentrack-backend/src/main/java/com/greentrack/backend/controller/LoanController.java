package com.greentrack.backend.controller;

import com.greentrack.backend.dto.LoanDTO;
import com.greentrack.backend.dto.LoanRequestDTO;
import com.greentrack.backend.entity.Loan;
import com.greentrack.backend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAll() {
        List<LoanDTO> dtos = loanService.getAll()
                .stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<LoanDTO>> filter(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate dateStart,
            @RequestParam(required = false) LocalDate dateEnd
    ) {
        List<LoanDTO> dtos = loanService.filter(userId, dateStart, dateEnd)
                .stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<LoanDTO> create(@Valid @RequestBody LoanRequestDTO dto) {
        Loan newLoan = loanService.create(dto);
        return ResponseEntity.ok(new LoanDTO(newLoan));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnItem(id);
        return ResponseEntity.ok(new LoanDTO(returnedLoan));
    }
}