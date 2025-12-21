package com.greentrack.backend.dto;

import com.greentrack.backend.entity.Equipment;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String type;
    private String brand;
    private String status;

    public EquipmentDTO(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.type = equipment.getType();
        this.brand = equipment.getBrand();
        this.status = equipment.getStatus().name();
    }
}