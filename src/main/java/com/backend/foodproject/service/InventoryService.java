package com.backend.foodproject.service;

import com.backend.foodproject.dto.stock.LowStockItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {

    Page<LowStockItemDto> getLowStock(Integer threshold, Pageable pageable);
    long countLowStock(Integer threshold);
}
