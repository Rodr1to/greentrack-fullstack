package com.greentrack.backend.service;

import com.greentrack.backend.dto.EquipmentDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.repository.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAll() {
        return equipmentRepository.findAll();
    }

    // filtro
    public List<Equipment> search(String keyword) {
        return equipmentRepository.search(keyword);
    }

    public Equipment create(EquipmentDTO dto) {
        // nombre unico
        if (equipmentRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre: " + dto.getName());
        }

        Equipment equipment = new Equipment();
        equipment.setName(dto.getName());
        equipment.setType(dto.getType());
        equipment.setBrand(dto.getBrand());
        equipment.setStatus(Equipment.Status.AVAILABLE); // disponible por default

        return equipmentRepository.save(equipment);
    }

    public Equipment update(Long id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        // si cambia nombre, validar que exista ya
        if (!existing.getName().equals(dto.getName()) && equipmentRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("El nombre ya está en uso por otro equipo");
        }

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setBrand(dto.getBrand());

        return equipmentRepository.save(existing);
    }

    public void delete(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado");
        }
        equipmentRepository.deleteById(id);
    }

    public List<String> getBrands() {
        return equipmentRepository.findDistinctBrands();
    }
}