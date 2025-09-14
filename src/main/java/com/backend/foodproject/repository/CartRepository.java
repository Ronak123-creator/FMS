package com.backend.foodproject.repository;

import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(UserInfo user);
    Optional<Cart> findByUserId(Integer userId);
}
