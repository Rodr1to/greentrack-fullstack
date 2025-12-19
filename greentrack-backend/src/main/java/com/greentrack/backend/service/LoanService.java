package com.greentrack.backend.service;

import com.greentrack.backend.dto.LoanRequest;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.entity.Loan;
import com.greentrack.backend.entity.User;
import com.greentrack.backend.repository.EquipmentRepository;
import com.greentrack.backend.repository.LoanRepository;
import com.greentrack.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional; // IMPORTANTE: De jakarta o org.springframework
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    @Transactional // Todo o nada
    public Loan create(LoanRequest request) {
        // 1. Buscar Usuario
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // 2. Buscar Equipo
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        // 3. VALIDACIÓN: ¿El equipo está disponible?
        if (equipment.getStatus() == Equipment.Status.PRESTADO) {
            throw new IllegalStateException("El equipo ya está prestado. No se puede asignar.");
        }

        // 4. Crear el Préstamo
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setEquipment(equipment);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        // 5. ACTUALIZAR ESTADO DEL EQUIPO (Efecto secundario)
        equipment.setStatus(Equipment.Status.PRESTADO);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnItem(Long loanId) {
        // buscar
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        if (loan.getStatus() == Loan.LoanStatus.DEVUELTO) {
            throw new IllegalStateException("Este préstamo ya fue devuelto anteriormente.");
        }

        // cambiar estado
        loan.setStatus(Loan.LoanStatus.DEVUELTO);
        loan.setReturnDate(LocalDate.now());

        // liberar equipo
        Equipment equipment = loan.getEquipment();
        equipment.setStatus(Equipment.Status.AVAILABLE);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }
}