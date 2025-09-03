package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.categoryDto.CategoryCreateDto;
import com.backend.foodproject.dto.categoryDto.CategoryDetailResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryResponseDto;
import com.backend.foodproject.dto.categoryDto.CategoryUpdateDto;
import com.backend.foodproject.service.CategoryService;
import com.backend.foodproject.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseUtils responseUtils;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@RequestBody CategoryCreateDto dto){
        CategoryResponseDto category = categoryService.createCategory(dto);
        return responseUtils.created(category);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategory(){
        List<CategoryResponseDto> category = categoryService.getAllCategory();
        return responseUtils.ok(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDetailResponseDto>> getCategoryById(@Valid @PathVariable int id){
        CategoryDetailResponseDto responseDto = categoryService.getCategoryById(id);
        return responseUtils.ok(responseDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryUpdateDto dto){
        CategoryResponseDto responseDto = categoryService.updateCategory(id,dto);
        return responseUtils.ok("Updated", responseDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
        return responseUtils.ok("Category with id "+ id +" is deleted", null);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/add/category/{categoryId}/food/{foodId}")
    public ResponseEntity<ApiResponse<Void>> addFoodItemToCategory(@PathVariable int categoryId,
                                                                   @PathVariable int foodId){
        categoryService.addFoodItemToCategory(categoryId,foodId);
        return responseUtils.ok("Food Item with id " + foodId + " has been added to category id " + categoryId, null);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/remove/category/{categoryId}/food/{foodId}")
    public ResponseEntity<ApiResponse<Void>> removeFoodItemFromCategory(@PathVariable int categoryId,
                                                                        @PathVariable int foodId){
        categoryService.removeFoodItemFromCategory(categoryId,foodId);
        return responseUtils.ok("Food Item with id " + foodId + " is removed from category id " + categoryId, null);
    }

}
