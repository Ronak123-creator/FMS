package com.backend.foodproject.mapper;

import com.backend.foodproject.dto.categoryDto.CategoryCreateDto;
import com.backend.foodproject.dto.categoryDto.CategoryDetailResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryResponseDto;
import com.backend.foodproject.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    //to dto
    public static CategoryResponseDto toDto(Category category){
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public static CategoryDetailResponseDto toDetatilDto(Category category){

        return new CategoryDetailResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getFoodItems().stream()
                        .map(FoodItemMapper::toDto)
                        .collect(Collectors.toList()),
                category.getCreatedAt(),
                category.getUpdatedAt()

        );
    }

    //to entity
    public static Category toEntity(CategoryCreateDto dto){
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    //list to dto
    public static List<CategoryResponseDto> toDtoList(List<Category> categories){
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
