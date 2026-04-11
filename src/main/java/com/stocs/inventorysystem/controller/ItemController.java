package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.model.Category;
import com.stocs.inventorysystem.model.Item;
import com.stocs.inventorysystem.model.Supplier;
import com.stocs.inventorysystem.repository.CategoryRepository;
import com.stocs.inventorysystem.repository.ItemRepository;
import com.stocs.inventorysystem.repository.SupplierRepository;
import com.stocs.inventorysystem.service.AlertService;
import com.stocs.inventorysystem.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;
    private final AlertService alertService;

    public ItemController(ItemRepository itemRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository, InventoryService inventoryService, AlertService alertService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryService = inventoryService;
        this.alertService = alertService;
    }

    @GetMapping
    public String list(Model model) {
        List<Item> items = itemRepository.findAll();
        Map<Long, Integer> stocks = inventoryService.getStocksForItems(items);
        Map<Long, String> status = alertService.statusByItemId(items);
        model.addAttribute("items", items);
        model.addAttribute("stocks", stocks);
        model.addAttribute("status", status);
        return "items/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "items/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Item item, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("suppliers", supplierRepository.findAll());
            return "items/form";
        }
        if (item.getCategory() != null && item.getCategory().getId() != null) {
            item.setCategory(categoryRepository.getReferenceById(item.getCategory().getId()));
        }
        if (item.getSupplier() != null && item.getSupplier().getId() != null) {
            item.setSupplier(supplierRepository.getReferenceById(item.getSupplier().getId()));
        }
        Item saved = itemRepository.save(item);
        if (saved.getSku() == null || saved.getSku().isBlank()) {
            saved.setSku(generateSku(saved));
            saved = itemRepository.save(saved);
        }
        return "redirect:/items";
    }

    private String generateSku(Item item) {
        String idPart = String.format("%06d", item.getId() == null ? 0 : item.getId());
        return "ITM-" + idPart;
    }
}
