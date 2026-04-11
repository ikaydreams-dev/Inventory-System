package com.stocs.inventorysystem.repository;

import com.stocs.inventorysystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
