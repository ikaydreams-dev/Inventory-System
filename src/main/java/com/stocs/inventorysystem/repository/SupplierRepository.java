package com.stocs.inventorysystem.repository;

import com.stocs.inventorysystem.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
