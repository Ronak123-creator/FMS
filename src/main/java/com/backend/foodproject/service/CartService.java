package com.backend.foodproject.service;

import com.backend.foodproject.dto.cart.AddToCart;
import com.backend.foodproject.dto.cart.CartItemResponseDto;
import com.backend.foodproject.dto.cart.UpdateCartItemDto;

public interface CartService {

    CartItemResponseDto getCart();
    CartItemResponseDto addToCart(AddToCart dto);
    CartItemResponseDto updateCartItem(UpdateCartItemDto dto);
    CartItemResponseDto removeFromCart(Integer bookId);
    void clearCart();
}
