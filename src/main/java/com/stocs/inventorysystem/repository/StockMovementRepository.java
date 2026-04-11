package com.stocs.inventorysystem.repository;

import com.stocs.inventorysystem.model.MovementType;
import com.stocs.inventorysystem.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query("select coalesce(sum(m.quantity),0) from StockMovement m where m.item.id = :itemId and m.type = 'RECEIPT'")
    Integer sumReceipts(@Param("itemId") Long itemId);

    @Query("select coalesce(sum(m.quantity),0) from StockMovement m where m.item.id = :itemId and m.type = 'ISSUE'")
    Integer sumIssues(@Param("itemId") Long itemId);

    List<StockMovement> findByItemIdOrderByOccurredAtDesc(Long itemId);

    @Query("select coalesce(sum(case when m.type='RECEIPT' then m.quantity else -m.quantity end),0) from StockMovement m where m.item.id = :itemId")
    Integer netQuantity(@Param("itemId") Long itemId);

    @Query("select coalesce(sum(m.quantity),0) from StockMovement m where m.type = :type and m.occurredAt between :from and :to")
    Integer sumByTypeAndDateRange(@Param("type") MovementType type, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
