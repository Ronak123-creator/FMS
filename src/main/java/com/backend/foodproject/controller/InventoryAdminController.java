package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.stock.LowStockItemDto;
import com.backend.foodproject.service.InventoryService;
import com.backend.foodproject.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stock")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class InventoryAdminController {
    private final InventoryService inventoryService;
    private final ResponseUtils responseUtils;

//    @GetMapping("/low")
//    public ResponseEntity<ApiResponse<Page<LowStockItemDto>>> getLowStock(
//            @RequestParam(value = "threshold", required = false) Integer threshold,
//            @PageableDefault(size = 20, sort = "quantity") Pageable pageable
//    ) {
//        Page<LowStockItemDto> lowStock = inventoryService.getLowStock(threshold, pageable);
//        return responseUtils.ok("Inventory Low Stock", lowStock);
//    }
@GetMapping("/low")
public ResponseEntity<ApiResponse<Page<LowStockItemDto>>> getLowStock(
        @RequestParam(value = "threshold", required = false) Integer threshold,
        @PageableDefault(size = 20) Pageable pageable // Remove sort from here
) {
    Pageable safePageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            JpaSort.unsafe(Sort.Direction.ASC, "quantity")
                    .and(Sort.by(Sort.Direction.ASC, "updatedAt"))
    );

    Page<LowStockItemDto> lowStock = inventoryService.getLowStock(threshold, safePageable);
    return responseUtils.ok("Inventory Low Stock", lowStock);
}

    @GetMapping("/low/count")
    public ResponseEntity<ApiResponse<Long>> getLowStockCount(
            @RequestParam(value = "threshold", required = false) Integer threshold
    ){
        Long count = inventoryService.countLowStock(threshold);
        return responseUtils.ok(count);
    }

}