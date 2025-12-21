package com.greentrack.backend.repository;

import com.greentrack.backend.dto.StockSummaryDTO;
import com.greentrack.backend.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    boolean existsByName(String name);

    // conteo completo
    @Query("SELECT COUNT(e) FROM Equipment e")
    long countTotal();

    @Query("SELECT COUNT(e) FROM Equipment e WHERE LOWER(e.status) = 'disponible'")
    long countAvailable();

    // marca
    @Query("SELECT COUNT(e) FROM Equipment e WHERE LOWER(e.brand) = LOWER(:brand)")
    long countByBrand(@Param("brand") String brand);

    @Query("SELECT COUNT(e) FROM Equipment e WHERE LOWER(e.brand) = LOWER(:brand) AND LOWER(e.status) = 'disponible'")
    long countByBrandAvailable(@Param("brand") String brand);

    // tipo
    @Query("SELECT COUNT(e) FROM Equipment e WHERE LOWER(e.type) = LOWER(:type)")
    long countByType(@Param("type") String type);

    @Query("SELECT COUNT(e) FROM Equipment e WHERE LOWER(e.type) = LOWER(:type) AND LOWER(e.status) = 'disponible'")
    long countByTypeAvailable(@Param("type") String type);

    // dropdowns
    @Query("SELECT DISTINCT e.brand FROM Equipment e")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT e.type FROM Equipment e")
    List<String> findDistinctTypes();

    // buscador
    @Query("SELECT e FROM Equipment e WHERE " +
            "(:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(e.type) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(e.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Equipment> search(@Param("keyword") String keyword);

    // stock
    @Query("SELECT new com.greentrack.backend.dto.StockSummaryDTO(" +
            "e.name, COUNT(e), " +
            "SUM(CASE WHEN LOWER(e.status) = 'disponible' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN LOWER(e.status) = 'prestado' THEN 1 ELSE 0 END)) " +
            "FROM Equipment e GROUP BY e.name")
    List<StockSummaryDTO> getStockSummary();

    List<Equipment> findByStatusIgnoreCase(String status);

    @Query("SELECT e FROM Equipment e WHERE " +
            "(:brand IS NULL OR e.brand = :brand) AND " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:status IS NULL OR e.status = :status)")
    List<Equipment> filter(@Param("brand") String brand,
                           @Param("type") String type,
                           @Param("status") Equipment.Status status);
}