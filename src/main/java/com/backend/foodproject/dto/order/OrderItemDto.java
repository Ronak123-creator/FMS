package com.backend.foodproject.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer foodItemId;
    private String foodName;
    private double unitPrice;
    private int quantity;
    private double lineTotal;
}
