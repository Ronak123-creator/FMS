package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.config.InventoryProperties;
import com.backend.foodproject.dto.stock.LowStockItemDto;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final FoodItemRepository foodItemRepository;
    private final InventoryProperties inventoryProperties;

    @Override
    public Page<LowStockItemDto> getLowStock(Integer threshold, Pageable pageable) {
        int th = (threshold == null || threshold < 0)
                ? inventoryProperties.getLowStockThreshold()
                : threshold;
        return foodItemRepository.findLowStock(th, pageable);
    }

    @Override
    public long countLowStock(Integer threshold) {
        int th = (threshold == null || threshold < 0)
                ? inventoryProperties.getLowStockThreshold()
                : threshold;
        return foodItemRepository.countByIsActiveTrueAndQuantityLessThanEqual(th);
    }
}
