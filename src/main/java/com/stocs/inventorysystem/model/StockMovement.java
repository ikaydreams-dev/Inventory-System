package com.stocs.inventorysystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Item item;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MovementType type;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Customer customer;

    @Min(1)
    private int quantity;

    private String reference;

    private String note;

    @NotNull
    private LocalDateTime occurredAt;
}
