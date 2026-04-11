package com.stocs.inventorysystem.service;

import com.stocs.inventorysystem.model.Customer;
import com.stocs.inventorysystem.model.Item;
import com.stocs.inventorysystem.model.MovementType;
import com.stocs.inventorysystem.model.StockMovement;
import com.stocs.inventorysystem.model.Supplier;
import com.stocs.inventorysystem.repository.CustomerRepository;
import com.stocs.inventorysystem.repository.ItemRepository;
import com.stocs.inventorysystem.repository.StockMovementRepository;
import com.stocs.inventorysystem.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {
    private final ItemRepository itemRepository;
    private final StockMovementRepository movementRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;

    public InventoryService(ItemRepository itemRepository, StockMovementRepository movementRepository, SupplierRepository supplierRepository, CustomerRepository customerRepository) {
        this.itemRepository = itemRepository;
        this.movementRepository = movementRepository;
        this.supplierRepository = supplierRepository;
        this.customerRepository = customerRepository;
    }

    public int getStockOnHand(Long itemId) {
        Integer receipts = movementRepository.sumReceipts(itemId);
        Integer issues = movementRepository.sumIssues(itemId);
        return (receipts == null ? 0 : receipts) - (issues == null ? 0 : issues);
    }

    public Map<Long, Integer> getStocksForItems(List<Item> items) {
        Map<Long, Integer> map = new HashMap<>();
        for (Item i : items) {
            map.put(i.getId(), getStockOnHand(i.getId()));
        }
        return map;
    }

    @Transactional
    public void receiveStock(Long itemId, Long supplierId, int quantity, String reference, LocalDate date) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();
        StockMovement m = StockMovement.builder()
                .item(item)
                .type(MovementType.RECEIPT)
                .supplier(supplier)
                .quantity(quantity)
                .reference(reference)
                .occurredAt(date == null ? LocalDateTime.now() : date.atStartOfDay())
                .build();
        movementRepository.save(m);
    }

    @Transactional
    public void issueStock(Long itemId, Long customerId, int quantity, String reference, LocalDate date) {
        int onHand = getStockOnHand(itemId);
        if (quantity > onHand) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        Item item = itemRepository.findById(itemId).orElseThrow();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        StockMovement m = StockMovement.builder()
                .item(item)
                .type(MovementType.ISSUE)
                .customer(customer)
                .quantity(quantity)
                .reference(reference)
                .occurredAt(date == null ? LocalDateTime.now() : date.atStartOfDay())
                .build();
        movementRepository.save(m);
    }
}
