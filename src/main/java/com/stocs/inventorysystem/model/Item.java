package com.stocs.inventorysystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sku;

    @NotBlank
    private String name;

    @ManyToOne
    @NotNull
    private Category category;

    @ManyToOne
    @NotNull
    private Supplier supplier;

    private String unit;

    private BigDecimal unitPrice;

    @Min(0)
    private int reorderLevel;

    @Min(0)
    private int maxLevel;
}
