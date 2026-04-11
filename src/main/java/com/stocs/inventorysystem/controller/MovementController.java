package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.repository.CustomerRepository;
import com.stocs.inventorysystem.repository.ItemRepository;
import com.stocs.inventorysystem.repository.SupplierRepository;
import com.stocs.inventorysystem.service.InventoryService;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping
public class MovementController {
    private final InventoryService inventoryService;
    private final ItemRepository itemRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;

    public MovementController(InventoryService inventoryService, ItemRepository itemRepository, SupplierRepository supplierRepository, CustomerRepository customerRepository) {
        this.inventoryService = inventoryService;
        this.itemRepository = itemRepository;
        this.supplierRepository = supplierRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/receive")
    public String receiveForm(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "movements/receive";
    }

    @PostMapping("/receive")
    public String receive(@RequestParam Long itemId,
                          @RequestParam Long supplierId,
                          @RequestParam @Min(1) int quantity,
                          @RequestParam(required = false) String reference,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        inventoryService.receiveStock(itemId, supplierId, quantity, reference, date);
        return "redirect:/items";
    }

    @GetMapping("/issue")
    public String issueForm(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());
        return "movements/issue";
    }

    @PostMapping("/issue")
    public String issue(@RequestParam Long itemId,
                        @RequestParam Long customerId,
                        @RequestParam @Min(1) int quantity,
                        @RequestParam(required = false) String reference,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        inventoryService.issueStock(itemId, customerId, quantity, reference, date);
        return "redirect:/items";
    }
}
