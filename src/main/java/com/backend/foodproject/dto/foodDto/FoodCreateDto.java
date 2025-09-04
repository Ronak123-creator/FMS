package com.backend.foodproject.dto.foodDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Category Id is required")
    private Integer categoryId;

    @NotNull(message = "Price is required")
    private Double price;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(0)
    private Integer quantity;

    @NotNull
    private Boolean isActive;

}
