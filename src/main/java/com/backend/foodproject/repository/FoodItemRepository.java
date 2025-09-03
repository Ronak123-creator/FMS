package com.backend.foodproject.repository;

import com.backend.foodproject.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    List<FoodItem> findByCategoryId(int categoryId);

    Page<FoodItem> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(
            String q1, String q2, Pageable pageable
    );

}
