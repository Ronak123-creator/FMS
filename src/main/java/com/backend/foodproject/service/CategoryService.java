package com.backend.foodproject.service;

import com.backend.foodproject.dto.categoryDto.CategoryCreateDto;
import com.backend.foodproject.dto.categoryDto.CategoryDetailResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryCreateDto dto);
    List<CategoryResponseDto> getAllCategory();
    CategoryDetailResponseDto getCategoryById(int id);
    CategoryResponseDto updateCategory(int id, CategoryUpdateDto dto);
    void deleteCategory(int id);
    void removeFoodItemFromCategory(Integer categoryId,Integer foodId );
    void addFoodItemToCategory(Integer categoryId, Integer foodId);
}


