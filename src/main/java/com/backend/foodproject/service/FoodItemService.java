package com.backend.foodproject.service;

import com.backend.foodproject.dto.foodDto.FoodCreateDto;
import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import com.backend.foodproject.dto.foodDto.FoodUpdateDto;
import com.backend.foodproject.dto.foodDto.InventoryUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FoodItemService {

    FoodResponseDto createFoodItems(FoodCreateDto dto);
    Page<FoodResponseDto> getAllFoodItems(int page, int size, String field);
    FoodResponseDto getFoodItemById(int id);
    FoodResponseDto updateFoodItem(int id,FoodUpdateDto dto);
    void deleteFoodItem(int id);
    void statusFoodItem(int id);
    FoodResponseDto getFoodByCategory(String category);
    FoodResponseDto updateFoodQuantity(InventoryUpdateDto dto);
}
