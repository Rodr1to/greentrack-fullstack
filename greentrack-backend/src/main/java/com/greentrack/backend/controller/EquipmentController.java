package com.greentrack.backend.controller;

import com.greentrack.backend.dto.EquipmentDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getAll() {
        return ResponseEntity.ok(equipmentService.getAll());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Equipment>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(equipmentService.search(keyword));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(equipmentService.getBrands());
    }

    @PostMapping
    public ResponseEntity<Equipment> create(@Valid @RequestBody EquipmentDTO dto) {
        return ResponseEntity.ok(equipmentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> update(@PathVariable Long id, @Valid @RequestBody EquipmentDTO dto) {
        return ResponseEntity.ok(equipmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}