package com.backend.foodproject.dto.categoryDto;

import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailResponseDto {

    private Integer id;
    private String name;
    private String description;
    private List<FoodResponseDto> foodItems;
    private Instant createdAt;
    private Instant updatedAt;

}
