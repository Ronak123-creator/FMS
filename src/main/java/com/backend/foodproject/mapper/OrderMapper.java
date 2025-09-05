package com.backend.foodproject.mapper;

import com.backend.foodproject.dto.order.OrderItemDto;
import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.entity.FoodItem;
import com.backend.foodproject.entity.Order;
import com.backend.foodproject.entity.OrderItem;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderItem toEntity(FoodItem foodItem, int quantity, Order order) {
        OrderItem oi = new OrderItem();
        oi.setOrder(order);
        oi.setFoodItem(foodItem);
        oi.setUnitPrice(foodItem.getPrice());          // snapshot price at order time
        oi.setQuantity(quantity);
        oi.setLineTotal(oi.getUnitPrice() * quantity);
        return oi;
    }

    public static OrderItemDto toItemDto(OrderItem orderItem){
        return new OrderItemDto(
                orderItem.getFoodItem().getId(),
                orderItem.getFoodItem().getName(),
                orderItem.getUnitPrice(),
                orderItem.getQuantity(),
                orderItem.getLineTotal()
        );
    }

    public static OrderResponseDto toDto(Order order){
        return new OrderResponseDto(
                order.getId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getNotes(),
                order.getDeliveryAddress(),
                order.getPhoneNumber(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(OrderMapper::toItemDto)
                        .collect(Collectors.toList())

        );
    }
}
