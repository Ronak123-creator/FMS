package com.backend.foodproject.dto.foodDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodUpdateDto {

    private String name;

    private Integer categoryId;

    private Double price;

    private String description;

    private Integer quantity;

    private Boolean isActive;
}
