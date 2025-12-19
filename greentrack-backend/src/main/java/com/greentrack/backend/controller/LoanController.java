package com.greentrack.backend.controller;

import com.greentrack.backend.dto.LoanRequest;
import com.greentrack.backend.entity.Loan;
import com.greentrack.backend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Loan>> getAll() {
        return ResponseEntity.ok(loanService.getAll());
    }

    @PostMapping
    public ResponseEntity<Loan> create(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.create(request));
    }

    // endpoint para devolver
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Loan> returnItem(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnItem(id));
    }
}