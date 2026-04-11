package com.stocs.inventorysystem.repository;

import com.stocs.inventorysystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
