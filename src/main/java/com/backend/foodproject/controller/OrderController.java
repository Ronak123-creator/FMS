package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.dto.order.PlaceOrderRequestDto;
import com.backend.foodproject.service.OrderService;
import com.backend.foodproject.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ResponseUtils responseUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> placeOrder(
            @RequestBody(required = false) PlaceOrderRequestDto dto
            ){
        OrderResponseDto order = orderService.placeOrder(dto);
        return responseUtils.ok("Order Placed", order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Integer id) {
        OrderResponseDto order = orderService.getOrder(id);
        return responseUtils.ok(order);
    }

    @GetMapping("/myOrder")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> myOrders() {
        List<OrderResponseDto> order = orderService.myOrders();
        return responseUtils.ok(order);
    }


    @PostMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(@PathVariable Integer id) {
        OrderResponseDto cancel = orderService.cancelOrder(id);
        return responseUtils.ok("Cancelled", cancel);
    }

}
