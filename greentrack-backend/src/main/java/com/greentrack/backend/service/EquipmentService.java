package com.greentrack.backend.service;

import com.greentrack.backend.dto.EquipmentDTO;
import com.greentrack.backend.dto.StockSummaryDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.repository.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAll() {
        return equipmentRepository.findAll();
    }

    public List<Equipment> search(String keyword) {
        return equipmentRepository.search(keyword);
    }

    // filtros
    public List<String> getBrands() {
        return equipmentRepository.findDistinctBrands();
    }

    public List<String> getTypes() {
        return equipmentRepository.findDistinctTypes();
    }

    // stock
    public List<StockSummaryDTO> getStockSummary() {
        return equipmentRepository.getStockSummary();
    }

    @Transactional
    public Equipment create(EquipmentDTO dto) {
        if (equipmentRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre: " + dto.getName());
        }
        Equipment equipment = new Equipment();
        equipment.setName(dto.getName());
        equipment.setBrand(dto.getBrand());
        equipment.setType(dto.getType());
        equipment.setStatus(Equipment.Status.DISPONIBLE); // por defecto
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public Equipment update(Long id, EquipmentDTO dto) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        equipment.setName(dto.getName());
        equipment.setBrand(dto.getBrand());
        equipment.setType(dto.getType());
        return equipmentRepository.save(equipment);
    }

    public void delete(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado");
        }
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> filter(String brand, String type, String statusStr) {
        Equipment.Status statusEnum = null;
        if (statusStr != null && !statusStr.isEmpty() && !statusStr.equals("TODOS")) {
            try {
                statusEnum = Equipment.Status.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                statusEnum = null;
            }
        }
        return equipmentRepository.filter(brand, type, statusEnum);
    }
}