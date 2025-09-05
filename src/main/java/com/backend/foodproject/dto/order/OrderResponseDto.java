package com.backend.foodproject.dto.order;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Integer id;
    private String status;
    private double totalPrice;
    private String notes;
    private String deliveryAddress;
    private String phoneNumber;
    private Instant createdAt;
    private List<OrderItemDto> items;
}
