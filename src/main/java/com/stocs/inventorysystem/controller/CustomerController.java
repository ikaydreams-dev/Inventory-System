package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.model.BusinessSize;
import com.stocs.inventorysystem.model.Customer;
import com.stocs.inventorysystem.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customers/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("sizes", BusinessSize.values());
        return "customers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sizes", BusinessSize.values());
            return "customers/form";
        }
        customerRepository.save(customer);
        return "redirect:/customers";
    }
}
