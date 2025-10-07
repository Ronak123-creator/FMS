package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.order.AdminUpdateOrderStatusDto;
import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.enums.OrderStatus;
import com.backend.foodproject.service.AdminOrderService;
import com.backend.foodproject.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final ResponseUtils responseUtils;


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrder(){
        List<OrderResponseDto> order = adminOrderService.getAllOrder();
        return responseUtils.ok("All Orders", order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> get(@PathVariable Integer id) {
        OrderResponseDto order = adminOrderService.get(id);
        return responseUtils.ok(order);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> search(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<OrderResponseDto> searchItem = adminOrderService.search(status, from, to, q, page, size);
        return responseUtils.ok("Search Data", searchItem);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateStatus(
            @PathVariable Integer id,
            @RequestBody AdminUpdateOrderStatusDto req
    ) {
        OrderResponseDto changeStatus = adminOrderService.updateStatus(id, req);
        return responseUtils.ok("Status updated", changeStatus);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancel(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "true") boolean restock,
            @RequestParam(required = false) String reason
    ) {
        OrderResponseDto cancelOrder = adminOrderService.cancel(id, reason, restock);
        return responseUtils.ok("Cancelled",cancelOrder);
    }

}
