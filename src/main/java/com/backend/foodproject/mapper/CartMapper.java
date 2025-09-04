package com.backend.foodproject.mapper;

import com.backend.foodproject.dto.cart.AddToCart;
import com.backend.foodproject.dto.cart.CartItemDto;
import com.backend.foodproject.dto.cart.CartItemResponseDto;
import com.backend.foodproject.dto.cart.UpdateCartItemDto;
import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.CartItem;
import com.backend.foodproject.entity.FoodItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartItem toEntity(AddToCart dto, FoodItem item, Cart cart){
        CartItem cartItem = new CartItem();

        cartItem.setFoodItem(item);
        cartItem.setCart(cart);
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }

    public static void updateEntity(CartItem cartItem, UpdateCartItemDto dto){
        cartItem.setQuantity(dto.getQuantity());
    }

    public static CartItemDto toCartItemDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getFoodItem().getId(),
                cartItem.getFoodItem().getName(),
                cartItem.getFoodItem().getCategory().getName(),
                cartItem.getFoodItem().getPrice(),
                cartItem.getQuantity(),
                cartItem.getQuantity() * cartItem.getFoodItem().getPrice()
        );
    }

    public static CartItemResponseDto toDto(Cart cart){

        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(CartMapper::toCartItemDto)
                .collect(Collectors.toList());

        Double totalAmount = (cart.getTotalPrice()!=null)
                ? cart.getTotalPrice()
                : itemDtos.stream().mapToDouble(CartItemDto::getSubTotal).sum();

        Integer totalItems = cart.getItems().stream()
                .mapToInt(ci -> ci.getQuantity() != null ? ci.getQuantity() : 0)
                .sum();

        return new CartItemResponseDto(
                cart.getId(),
                cart.getUser().getId(),
                cart.getUser().getEmail(),
                itemDtos,
                totalAmount,
                totalItems,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );

    }
}
