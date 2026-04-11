package com.stocs.inventorysystem.controller;

import com.stocs.inventorysystem.model.MovementType;
import com.stocs.inventorysystem.repository.StockMovementRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/reports")
public class ReportController {
    private final StockMovementRepository movementRepository;

    public ReportController(StockMovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @GetMapping("/summary")
    public String summary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                          Model model) {
        LocalDate start = from != null ? from : LocalDate.now().minusDays(30);
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDateTime fromDt = start.atStartOfDay();
        LocalDateTime toDt = end.atTime(23,59,59);
        Integer received = movementRepository.sumByTypeAndDateRange(MovementType.RECEIPT, fromDt, toDt);
        Integer issued = movementRepository.sumByTypeAndDateRange(MovementType.ISSUE, fromDt, toDt);
        model.addAttribute("from", start);
        model.addAttribute("to", end);
        model.addAttribute("received", received == null ? 0 : received);
        model.addAttribute("issued", issued == null ? 0 : issued);
        return "reports/summary";
    }
}
