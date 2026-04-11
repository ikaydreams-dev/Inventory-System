package com.stocs.inventorysystem.repository;

import com.stocs.inventorysystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
