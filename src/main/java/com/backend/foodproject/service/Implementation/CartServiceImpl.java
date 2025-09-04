package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.dto.cart.AddToCart;
import com.backend.foodproject.dto.cart.CartItemResponseDto;
import com.backend.foodproject.dto.cart.UpdateCartItemDto;
import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.CartItem;
import com.backend.foodproject.entity.FoodItem;
import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.CartMapper;
import com.backend.foodproject.repository.CartItemRepository;
import com.backend.foodproject.repository.CartRepository;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.repository.UserInfoRepository;
import com.backend.foodproject.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserInfoRepository userInfoRepository;
    private final FoodItemRepository foodItemRepository;

    private UserInfo getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        return userInfoRepository.findByEmail(email)
                .orElseThrow(()-> new CustomExceptionHandling("User Not Found",
                        HttpStatus.NOT_FOUND.value()));
    }

    private Cart getOrCreateCart(UserInfo user){
        if(user.getCart() == null){
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
        return user.getCart();
    }

    private void calculatePrice(Cart cart){
        double total = 0.0;
        for(CartItem i : cart.getItems()){
            double unitPrice = i.getFoodItem().getPrice();
            int qun = i.getQuantity();
            double price = unitPrice * qun;
            i.setItemTotal(price);
            total += price;

        }
        cart.setTotalPrice(total);
    }


    @Override
    public CartItemResponseDto getCart() {
        UserInfo user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartItemResponseDto addToCart(AddToCart dto) {
        UserInfo user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        FoodItem foodItem = foodItemRepository.findById(dto.getFoodItemId())
                .orElseThrow(()-> new CustomExceptionHandling("Food Item Not Found",
                        HttpStatus.BAD_REQUEST.value()));


        Optional<CartItem> existing = cartItemRepository.findByCartAndFoodItem(cart, foodItem);

        if(existing.isPresent()){
            CartItem cartItem = existing.get();
            int newQuantity = cartItem.getQuantity() + dto.getQuantity();

            if(foodItem.getQuantity()<newQuantity){
                throw new CustomExceptionHandling("Insufficient Stock",
                        HttpStatus.BAD_REQUEST.value());
            }

            cartItem.setQuantity(newQuantity);
        }
        else {
            CartItem cartItem = CartMapper.toEntity(dto, foodItem,cart);
            cart.getItems().add(cartItem);
        }
        calculatePrice(cart);

        cartRepository.save(cart);
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartItemResponseDto updateCartItem(UpdateCartItemDto dto) {
        UserInfo user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        FoodItem foodItem = foodItemRepository.findById(dto.getFoodItemId())
                .orElseThrow(() -> new CustomExceptionHandling("Food Item not found",
                        HttpStatus.NOT_FOUND.value()));

        CartItem cartItem = cartItemRepository.findByCartAndFoodItem(cart, foodItem)
                .orElseThrow(() -> new CustomExceptionHandling("Item not found in cart",
                        HttpStatus.NOT_FOUND.value()));

        cartItem.setQuantity(dto.getQuantity());

        calculatePrice(cart);
        cartRepository.save(cart);
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartItemResponseDto removeFromCart(Integer bookId) {
        UserInfo user =getCurrentUser();
        Cart cart = getOrCreateCart(user);

        FoodItem foodItem = foodItemRepository.findById(bookId)
                .orElseThrow(() -> new CustomExceptionHandling("Food Item not found",
                        HttpStatus.NOT_FOUND.value()));

        CartItem item = cartItemRepository.findByCartAndFoodItem(cart,foodItem)
                .orElseThrow(()-> new CustomExceptionHandling("Item not found ",
                        HttpStatus.BAD_REQUEST.value()));

        cart.getItems().remove(item);
        calculatePrice(cart);

        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void clearCart() {
        UserInfo user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()->new CustomExceptionHandling("Cart Not Found ",
                        HttpStatus.NOT_FOUND.value()));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
