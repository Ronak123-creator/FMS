package com.backend.foodproject.dto.foodDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponseDto {

    private Integer id;
    private String name;
    private String categoryName;
    private double price;
    private String description;
    private Integer quantity;
    private Boolean isActive;
    private Boolean available;
    private Instant createdAt;
    private Instant updatedAt;

}
