package com.greentrack.backend.controller;

import com.greentrack.backend.dto.EquipmentDTO;
import com.greentrack.backend.dto.StockSummaryDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipos")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAll() {
        List<EquipmentDTO> dtos = equipmentService.getAll()
                .stream()
                .map(EquipmentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<EquipmentDTO>> filter(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        if ("TODOS".equals(brand) || "".equals(brand)) brand = null;
        if ("TODOS".equals(type) || "".equals(type)) type = null;
        if ("TODOS".equals(status) || "".equals(status)) status = null;

        List<EquipmentDTO> dtos = equipmentService.filter(brand, type, status)
                .stream()
                .map(EquipmentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(equipmentService.getBrands());
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getTypes() {
        return ResponseEntity.ok(equipmentService.getTypes());
    }

    @GetMapping("/stock-summary")
    public ResponseEntity<List<StockSummaryDTO>> getStockSummary() {
        return ResponseEntity.ok(equipmentService.getStockSummary());
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> create(@Valid @RequestBody EquipmentDTO dto) {
        Equipment created = equipmentService.create(dto);
        return ResponseEntity.ok(new EquipmentDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> update(@PathVariable Long id, @Valid @RequestBody EquipmentDTO dto) {
        Equipment updated = equipmentService.update(id, dto);
        return ResponseEntity.ok(new EquipmentDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}