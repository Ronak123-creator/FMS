package com.backend.foodproject.service;

import com.backend.foodproject.dto.foodDto.FoodCreateDto;
import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import com.backend.foodproject.dto.foodDto.FoodUpdateDto;
import com.backend.foodproject.dto.foodDto.InventoryUpdateDto;
import com.backend.foodproject.dto.qr.PublicMenuDto;
import org.springframework.data.domain.Page;

public interface FoodItemService {

    FoodResponseDto createFoodItems(FoodCreateDto dto);
    Page<FoodResponseDto> getAllFoodItems(int page, int size, String field);
    FoodResponseDto getFoodItemById(int id);
    FoodResponseDto updateFoodItem(int id,FoodUpdateDto dto);
    void deleteFoodItem(int id);
    void statusFoodItem(int id);
    FoodResponseDto getFoodByCategory(String category);
    FoodResponseDto updateFoodQuantity(InventoryUpdateDto dto);
    Page<FoodResponseDto> searchFoodItem(String s, int page, int size);
    PublicMenuDto getMenu();

}
