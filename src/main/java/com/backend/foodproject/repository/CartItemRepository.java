package com.backend.foodproject.repository;

import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.CartItem;
import com.backend.foodproject.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartAndFoodItem(Cart cart, FoodItem foodItem);
    List<CartItem> findByCart(Cart cart);
    void deleteByCartAndFoodItem(Cart cart, FoodItem foodItem);
    void deleteAllByCart(Cart cart);
}
