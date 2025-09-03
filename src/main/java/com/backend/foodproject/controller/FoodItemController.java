package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.foodDto.FoodCreateDto;
import com.backend.foodproject.dto.foodDto.FoodResponseDto;
import com.backend.foodproject.dto.foodDto.FoodUpdateDto;
import com.backend.foodproject.dto.foodDto.InventoryUpdateDto;
import com.backend.foodproject.service.FoodItemService;
import com.backend.foodproject.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodItemController {

    private final FoodItemService foodItemService;
    private final ResponseUtils responseUtils;

    @GetMapping("/hello")
    public String hello(){
        return "Welcome to food project";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<FoodResponseDto>> createFoodItem(@RequestBody FoodCreateDto dto){
        FoodResponseDto food = foodItemService.createFoodItems(dto);
        return responseUtils.created(food);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FoodResponseDto>>> getAllFoodItem(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(required = false) String field
    ){
        Page<FoodResponseDto> result = foodItemService.getAllFoodItems(page, size, field);
        return responseUtils.ok(result);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<FoodResponseDto>> updateFoodItem(@PathVariable int id,
                                                                       @RequestBody FoodUpdateDto dto){
        FoodResponseDto food = foodItemService.updateFoodItem(id, dto);
        return responseUtils.ok("Updated ", food);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/quantity")
    public ResponseEntity<ApiResponse<FoodResponseDto>> updateQuantity(@RequestBody InventoryUpdateDto dto){
        FoodResponseDto update = foodItemService.updateFoodQuantity(dto);
        return responseUtils.ok("Quantity Update Successfully", update);

    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<FoodResponseDto>>> searchFoodItem(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<FoodResponseDto> foodList = foodItemService.searchFoodItem(searchTerm,page,size);
        return responseUtils.ok(foodList);
    }

}
