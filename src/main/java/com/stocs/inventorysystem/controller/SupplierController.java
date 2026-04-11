package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.model.Supplier;
import com.stocs.inventorysystem.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "suppliers/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("supplier") Supplier supplier, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "suppliers/form";
        }
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }
}
