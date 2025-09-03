package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.dto.foodDto.FoodCreateDto;
import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import com.backend.foodproject.dto.foodDto.FoodUpdateDto;
import com.backend.foodproject.dto.foodDto.InventoryUpdateDto;
import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.FoodItem;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.FoodItemMapper;
import com.backend.foodproject.repository.CategoryRepository;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.service.FoodItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemServiceImpl implements FoodItemService {
    private final FoodItemRepository foodItemRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public FoodResponseDto createFoodItems(FoodCreateDto dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new CustomExceptionHandling("Category not Found",
                        HttpStatus.BAD_REQUEST.value()));
        FoodItem item = FoodItemMapper.toEntity(dto,category);
        foodItemRepository.save(item);
        return FoodItemMapper.toDto(item);
    }

    @Override
    public Page<FoodResponseDto> getAllFoodItems(int page, int size, String field) {
        if(page<0){
            throw new CustomExceptionHandling("Page index must be >= 0", HttpStatus.BAD_REQUEST.value());
        }
        if(size <= 0){
            throw new CustomExceptionHandling("Size must be > 0", HttpStatus.BAD_REQUEST.value());
        }
        Sort sort;
        if(field != null && !field.isBlank()){
            sort = Sort.by(field).ascending();
        }
        else {
            sort = Sort.by(Sort.Direction.DESC,"updatedAt");

        }
        Pageable pageable = PageRequest.of(page,size, sort);

        Page<FoodItem> foodPage = foodItemRepository.findAll(pageable);

        if(page > foodPage.getTotalPages()){
            throw new CustomExceptionHandling("Requested Page " + page + " exceed total page " + foodPage.getTotalPages(),
                    HttpStatus.BAD_REQUEST.value());

        }

        List<FoodResponseDto> dtos = foodPage.getContent()
                .stream()
                .map(FoodItemMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, foodPage.getTotalElements());

    }

    @Override
    public FoodResponseDto getFoodItemById(int id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(()-> new CustomExceptionHandling("Error " + id, HttpStatus.NOT_FOUND.value()));
        return FoodItemMapper.toDto(foodItem);
    }

    @Override
    public FoodResponseDto updateFoodItem(int id, FoodUpdateDto dto) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(()->new CustomExceptionHandling("Food not found with id : "+ id, HttpStatus.NOT_FOUND.value()));

        if(dto.getName() != null)         foodItem.setName(dto.getName());
        if(dto.getPrice() != null)        foodItem.setPrice(dto.getPrice());
        if(dto.getDescription() != null)  foodItem.setDescription(dto.getDescription());
        if(dto.getQuantity() != null)     foodItem.setQuantity(dto.getQuantity());
        if(dto.getIsActive() != null)     foodItem.setIsActive(dto.getIsActive());

        foodItemRepository.save(foodItem);
        return FoodItemMapper.toDto(foodItem);

    }

    @Override
    public void deleteFoodItem(int id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(()->new CustomExceptionHandling("Food not found with id : "+ id, HttpStatus.NOT_FOUND.value()));

        foodItemRepository.delete(foodItem);
    }

    @Override
    @Transactional
    public void statusFoodItem(int id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionHandling(
                        "Food Item not found: " + id,
                        HttpStatus.NOT_FOUND.value()));

        // Toggle the active status
        foodItem.setIsActive(!foodItem.getIsActive());

        foodItemRepository.save(foodItem);
    }

    @Override
    public FoodResponseDto getFoodByCategory(String category) {
        return null;
    }

    @Override
    public FoodResponseDto updateFoodQuantity(InventoryUpdateDto dto) {
        FoodItem foodItem = foodItemRepository.findById(dto.getFoodId())
                .orElseThrow(()-> new CustomExceptionHandling("Error " + dto.getFoodId(),
                        HttpStatus.NOT_FOUND.value()));

        int updatedQuantity = foodItem.getQuantity() + dto.getQuantityChange();
        if(updatedQuantity< 0){
            throw  new CustomExceptionHandling("Insufficient Stock",HttpStatus.BAD_REQUEST.value());
        }

        foodItem.setQuantity(updatedQuantity);
        foodItemRepository.save(foodItem);
        return FoodItemMapper.toDto(foodItem);
    }

   @Override
    public Page<FoodResponseDto> searchFoodItem(String s, int page, int size){
        String searchTerm = (s == null) ? "": s.trim();
        if(searchTerm.isEmpty()) throw  new CustomExceptionHandling("Not Found",HttpStatus.BAD_REQUEST.value());

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<FoodItem> result = foodItemRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(searchTerm,searchTerm,pageable);
       return result.map(FoodItemMapper::toDto);

   }
}
