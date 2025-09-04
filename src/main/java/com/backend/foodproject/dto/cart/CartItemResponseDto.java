package com.backend.foodproject.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemResponseDto {
    private Integer cartId;
    private Integer userId;
    private String userEmail;
    private List<CartItemDto> items;
    private Double totalAmount;
    private Integer totalItems;
    private Instant createdAt;
    private Instant updatedAt;
}
