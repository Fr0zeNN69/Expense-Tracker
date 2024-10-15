package com.example.webapp.controller;

import com.example.webapp.model.Expense;
import com.example.webapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication != null) {
            String username = authentication.getName();  // obtine utilizatorul conectat
            model.addAttribute("username", username);  // Adauga username-ul in model

            // Preia toate cheltuielile utilizatorului conectat
            List<Expense> expenses = expenseRepository.findByUsername(username);
            model.addAttribute("expenses", expenses);  // Afiseaza cheltuielile în tabel

            // Calculează suma totală a cheltuielilor
            double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
            model.addAttribute("totalAmount", totalAmount);  // Adauga suma totala in model
        }

        // Adauga un obiect gol pentru formularul de cheltuieli
        model.addAttribute("newExpense", new Expense());
        return "home";  // Returnează pagina home.html
    }

    @PostMapping("/addExpense")
    public String addExpense(@ModelAttribute("newExpense") Expense expense,
                             Authentication authentication) {
        String username = authentication.getName();  // Obtine utilizatorul conectat

        // Seteaza utilizatorul si data pentru cheltuiala
        expense.setUsername(username);
        expense.setDate(LocalDate.now());

        // Salvează cheltuiala in baza de date
        expenseRepository.save(expense);

        // Redirectioneaza inapoi la pagina de home pt a vedea cheltuielile actualizate
        return "redirect:/home";
    }

    @PostMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        // sterge cheltuiala pe baza id-ului
        expenseRepository.deleteById(id);

        // Redirectionează inapoi la pagina de home
        return "redirect:/home";
    }
}
