package com.greentrack.backend.service;

import com.greentrack.backend.dto.EquipmentDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository; // simular db

    @InjectMocks
    private EquipmentService equipmentService; // servicio a probar

    @Test
    void createEquipment_Success() {
        // datos prueba
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Laptop Dell");
        dto.setType("Laptop");
        dto.setBrand("Dell");

        // simular equipo guardado con id 1
        Equipment savedEquipment = new Equipment();
        savedEquipment.setId(1L);
        savedEquipment.setName("Laptop Dell");
        savedEquipment.setStatus(Equipment.Status.AVAILABLE);

        // simular comportamiento repositorio
        when(equipmentRepository.existsByName("Laptop Dell")).thenReturn(false);
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(savedEquipment);

        // ejecutar metodo when
        Equipment result = equipmentService.create(dto);

        // verificar resultados then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop Dell", result.getName());
        assertEquals(Equipment.Status.AVAILABLE, result.getStatus());

        // verificar llamada al repositorio
        verify(equipmentRepository).save(any(Equipment.class));
    }

    @Test
    void createEquipment_ThrowsError_WhenDuplicate() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Laptop Dell");

        // simular ya existe
        when(equipmentRepository.existsByName("Laptop Dell")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            equipmentService.create(dto);
        });

        // verificar no se guardo
        verify(equipmentRepository, never()).save(any());
    }
}