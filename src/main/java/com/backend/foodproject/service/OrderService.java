package com.backend.foodproject.service;

import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.dto.order.PlaceOrderRequestDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderRequestDto dto);
    OrderResponseDto getOrder(Integer id);
    List<OrderResponseDto> myOrders();
    OrderResponseDto cancelOrder(Integer id);
}
