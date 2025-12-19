package com.greentrack.backend.repository;

import com.greentrack.backend.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // validacion de duplicados
    boolean existsByName(String name);

    // conteo
    long countByStatus(String status);

    // filtro por marca

    long countByBrand(String brand);
    long countByBrandAndStatus(String brand, String status);

    // filtro/buscador
    @Query("SELECT e FROM Equipment e WHERE " +
            "(:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(e.type) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(e.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Equipment> search(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT e.brand FROM Equipment e")
    List<String> findDistinctBrands();
}