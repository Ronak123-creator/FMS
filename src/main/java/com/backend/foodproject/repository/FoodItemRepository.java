package com.backend.foodproject.repository;

import com.backend.foodproject.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    List<FoodItem> findByCategoryId(int categoryId);
}
