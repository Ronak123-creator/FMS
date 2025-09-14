package com.backend.foodproject.dto.qr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicFoodItemDto {
    private Integer id;
    private String name;
    private double price;
    private String description;
}
