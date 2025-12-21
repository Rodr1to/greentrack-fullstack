package com.greentrack.backend.service;

import com.greentrack.backend.dto.LoanRequestDTO;
import com.greentrack.backend.entity.Equipment;
import com.greentrack.backend.entity.Loan;
import com.greentrack.backend.entity.User;
import com.greentrack.backend.repository.EquipmentRepository;
import com.greentrack.backend.repository.LoanRepository;
import com.greentrack.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    // filtro
    public List<Loan> filter(Long userId, LocalDate dateStart, LocalDate dateEnd) {
        if (userId != null) {
            return loanRepository.findByUserId(userId);
        }
        if (dateStart != null && dateEnd != null) {
            return loanRepository.findByDateRange(dateStart, dateEnd);
        }
        return loanRepository.findAll();
    }

    @Transactional
    public Loan create(LoanRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        if (equipment.getStatus() != Equipment.Status.DISPONIBLE) {
            throw new IllegalStateException("El equipo no está disponible (Estado actual: " + equipment.getStatus() + ")");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setEquipment(equipment);
        loan.setLoanDate(request.getLoanDate() != null ? request.getLoanDate() : LocalDate.now());
        loan.setStatus(Loan.LoanStatus.ACTIVO);

        equipment.setStatus(Equipment.Status.PRESTADO);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnItem(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        if (loan.getStatus() == Loan.LoanStatus.DEVUELTO) {
            throw new IllegalStateException("Este préstamo ya fue devuelto anteriormente.");
        }

        loan.setStatus(Loan.LoanStatus.DEVUELTO);
        loan.setReturnDate(LocalDate.now());

        Equipment equipment = loan.getEquipment();
        equipment.setStatus(Equipment.Status.DISPONIBLE);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }
}