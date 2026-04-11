package com.stocs.inventorysystem.service;

import com.stocs.inventorysystem.model.Item;
import com.stocs.inventorysystem.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {
    private final ItemRepository itemRepository;
    private final InventoryService inventoryService;

    public AlertService(ItemRepository itemRepository, InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.inventoryService = inventoryService;
    }

    public List<Item> lowStockItems() {
        List<Item> result = new ArrayList<>();
        for (Item i : itemRepository.findAll()) {
            int stock = inventoryService.getStockOnHand(i.getId());
            if (stock <= i.getReorderLevel()) {
                result.add(i);
            }
        }
        return result;
    }

    public List<Item> overStockItems() {
        List<Item> result = new ArrayList<>();
        for (Item i : itemRepository.findAll()) {
            int stock = inventoryService.getStockOnHand(i.getId());
            if (i.getMaxLevel() > 0 && stock >= i.getMaxLevel()) {
                result.add(i);
            }
        }
        return result;
    }

    public Map<Long, String> statusByItemId(List<Item> items) {
        Map<Long, String> map = new HashMap<>();
        for (Item i : items) {
            int stock = inventoryService.getStockOnHand(i.getId());
            if (stock <= i.getReorderLevel()) {
                map.put(i.getId(), "LOW");
            } else if (i.getMaxLevel() > 0 && stock >= i.getMaxLevel()) {
                map.put(i.getId(), "HIGH");
            } else {
                map.put(i.getId(), "OK");
            }
        }
        return map;
    }
}
