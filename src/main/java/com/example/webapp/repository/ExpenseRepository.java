package com.example.webapp.repository;

import com.example.webapp.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUsername(String username);
    void deleteById(Long id);  // Adauga aceasta metoda pentru a sterge cheltuieli
}
