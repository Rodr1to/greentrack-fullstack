package com.greentrack.backend.controller;

import com.greentrack.backend.dto.StatsDTO;
import com.greentrack.backend.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final EquipmentRepository equipmentRepository;

    @GetMapping("/stats")
    public StatsDTO getStats(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type
    ) {
        long total = 0;
        long disponibles = 0;

        // por marca > tipo > global
        if (brand != null && !brand.isEmpty() && !brand.equals("TODOS")) {
            total = equipmentRepository.countByBrand(brand);
            disponibles = equipmentRepository.countByBrandAvailable(brand);
        }
        else if (type != null && !type.isEmpty() && !type.equals("TODOS")) {
            total = equipmentRepository.countByType(type);
            disponibles = equipmentRepository.countByTypeAvailable(type);
        }
        else {
            total = equipmentRepository.countTotal();
            disponibles = equipmentRepository.countAvailable();
        }

        long prestados = total - disponibles;
        return new StatsDTO(total, disponibles, prestados);
    }
}
