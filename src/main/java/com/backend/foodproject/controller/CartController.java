package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.cart.AddToCart;
import com.backend.foodproject.dto.cart.CartItemResponseDto;
import com.backend.foodproject.dto.cart.UpdateCartItemDto;
import com.backend.foodproject.service.CartService;
import com.backend.foodproject.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class CartController {
    private final CartService cartService;
    private final ResponseUtils responseUtils;

    @GetMapping
    public ResponseEntity<ApiResponse<CartItemResponseDto>> getCart(){
        CartItemResponseDto cartItemResponseDto = cartService.getCart();
        return responseUtils.ok(cartItemResponseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> addToCart(
            @Valid @RequestBody AddToCart dto
    ){
        System.out.println("Received DTO: " + dto);
        CartItemResponseDto cartItemResponseDto = cartService.addToCart(dto);
        return responseUtils.ok(cartItemResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> updateCart(
            @Valid @RequestBody UpdateCartItemDto dto){
        CartItemResponseDto cartItemResponseDto = cartService.updateCartItem(dto);
        return responseUtils.ok(cartItemResponseDto);
    }

    @DeleteMapping("/remove/{foodItemId}")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> removeFromCart(@PathVariable Integer foodItemId) {
        cartService.removeFromCart(foodItemId);
        return responseUtils.ok("Removed from cart",null);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(){
        cartService.clearCart();
        return responseUtils.noContent();
    }


}
