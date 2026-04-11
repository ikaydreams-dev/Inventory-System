package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.model.Item;
import com.stocs.inventorysystem.repository.ItemRepository;
import com.stocs.inventorysystem.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AnalyticsController {
    private final ItemRepository itemRepository;
    private final InventoryService inventoryService;

    public AnalyticsController(ItemRepository itemRepository, InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        List<Item> items = itemRepository.findAll();
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        for (Item i : items) {
            int stock = inventoryService.getStockOnHand(i.getId());
            labels.add(i.getName());
            values.add(1);
            if (stock == 0) {
                colors.add("#dc2626");
            } else if (stock <= i.getReorderLevel()) {
                colors.add("#eab308");
            } else {
                colors.add("#16a34a");
            }
        }
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        model.addAttribute("colors", colors);
        return "analytics";
    }
}
