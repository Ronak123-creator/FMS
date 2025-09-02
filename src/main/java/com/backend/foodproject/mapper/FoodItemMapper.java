package com.backend.foodproject.mapper;

import com.backend.foodproject.dto.foodDto.FoodCreateDto;
import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import com.backend.foodproject.dto.foodDto.FoodUpdateDto;
import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.FoodItem;

import java.util.List;
import java.util.stream.Collectors;

public class FoodItemMapper {
    //dto to entity
    public static FoodItem toEntity(FoodCreateDto dto, Category category){
        FoodItem item = new FoodItem();
        item.setName(dto.getName());
        item.setCategory(category);
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setQuantity(dto.getQuantity());
        item.setIsActive(dto.getIsActive());

        return item;
    }

    //entity to dto
    public static FoodResponseDto toDto(FoodItem item) {

        boolean available = Boolean.TRUE.equals(item.getIsActive())
                &&item.getQuantity()!=null && item.getQuantity()>0;
        return new FoodResponseDto(
                item.getId(),
                item.getName(),
                item.getCategory().getName(),
                item.getPrice(),
                item.getDescription(),
                item.getQuantity(),
                item.getIsActive(),
                available,
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    //list to dto
    public static List<FoodResponseDto> toDtoList(List<FoodItem> items){
        return items.stream().map(FoodItemMapper::toDto).collect(Collectors.toList());
    }
}
