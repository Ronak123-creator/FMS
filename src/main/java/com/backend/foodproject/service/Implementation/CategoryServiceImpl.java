package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.dto.categoryDto.CategoryCreateDto;
import com.backend.foodproject.dto.categoryDto.CategoryDetailResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryUpdateDto;
import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.FoodItem;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.CategoryMapper;
import com.backend.foodproject.repository.CategoryRepository;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FoodItemRepository foodItemRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto dto) {
        if(categoryRepository.existsByName(dto.getName())){
            throw new CustomExceptionHandling("Category Already Exists",
                    HttpStatus.CONFLICT.value());
        }
        Category category = CategoryMapper.toEntity(dto);
        categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategory() {
       return categoryRepository.findAll().stream()
               .map(CategoryMapper::toDto)
               .collect(Collectors.toList());
    }

    @Override
    public CategoryDetailResponseDto getCategoryById(int id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(()->new CustomExceptionHandling("Category Not found with id: " + id, HttpStatus.BAD_REQUEST.value()));
        return CategoryMapper.toDetatilDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(int id, CategoryUpdateDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CustomExceptionHandling("Category Not Found with id: "+id,
                        HttpStatus.BAD_REQUEST.value()));

        category.setName(dto.getName());
        if(dto.getDescription() != null){
            category.setDescription(dto.getDescription());
        }
        categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CustomExceptionHandling("Category Not Found with id: "+id,
                        HttpStatus.BAD_REQUEST.value()));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void removeFoodItemFromCategory(Integer categoryId, Integer foodId) {
        FoodItem foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(()->new CustomExceptionHandling("Food Item not found : " + foodId,
                        HttpStatus.BAD_REQUEST.value()));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CustomExceptionHandling("Category not found : " + foodId,
                        HttpStatus.BAD_REQUEST.value()));

        if (!foodItem.getCategory().getId().equals(categoryId)) {
            throw new CustomExceptionHandling("Food Id: " + foodId + " is not available in category id : " + categoryId,
                    HttpStatus.BAD_REQUEST.value());
        }

        category.getFoodItems().remove(foodItem);

        foodItemRepository.delete(foodItem);
        categoryRepository.save(category);

    }

    @Override
    @Transactional
    public void addFoodItemToCategory(Integer categoryId, Integer foodId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomExceptionHandling(
                        "Category not found: " + categoryId, HttpStatus.BAD_REQUEST.value()));

        FoodItem food = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new CustomExceptionHandling(
                        "Food item not found: " + foodId, HttpStatus.BAD_REQUEST.value()));


        if (food.getCategory() != null && food.getCategory().getId().equals(categoryId)) {
            throw new CustomExceptionHandling(
                    "Food id " + foodId + " is already in category id " + categoryId,
                    HttpStatus.CONFLICT.value()
            );
        }

        //Remove from old category
        Category oldCategory = food.getCategory();
        if(oldCategory != null){
            oldCategory.getFoodItems().remove(food);
            categoryRepository.save(oldCategory);
        }

        //Add to new category
        food.setCategory(category);
        category.getFoodItems().add(food);

        foodItemRepository.save(food);
        categoryRepository.save(category);

    }

}
