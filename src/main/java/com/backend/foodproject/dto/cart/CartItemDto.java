package com.backend.foodproject.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer id;
    private Integer foodItemId;
    private String foodItemName;
    private String foodItemCategory;
    private double foodItemPrice;
    private Integer quantity;
    private Double subTotal;

}
