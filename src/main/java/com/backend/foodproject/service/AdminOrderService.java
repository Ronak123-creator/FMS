package com.backend.foodproject.service;

import com.backend.foodproject.dto.order.AdminUpdateOrderStatusDto;
import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

public interface AdminOrderService {
    List<OrderResponseDto> getAllOrder();
    Page<OrderResponseDto> search(OrderStatus status, Instant from, Instant to, String q,int page, int size);
    OrderResponseDto get(Integer id);
    OrderResponseDto updateStatus(Integer id, AdminUpdateOrderStatusDto dto);
    OrderResponseDto cancel(Integer id, String reason, boolean restock);
}
