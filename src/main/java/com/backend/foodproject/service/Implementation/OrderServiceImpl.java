package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.dto.order.PlaceOrderRequestDto;
import com.backend.foodproject.entity.*;
import com.backend.foodproject.enums.OrderStatus;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.OrderMapper;
import com.backend.foodproject.repository.CartRepository;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.repository.OrderRepository;
import com.backend.foodproject.repository.UserInfoRepository;
import com.backend.foodproject.service.OrderService;
import com.backend.foodproject.service.auth.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserInfoRepository userInfoRepository;
    private final EmailService emailService;

    private UserInfo currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptionHandling("User Not Found",
                        HttpStatus.NOT_FOUND.value()));
    }

    private String buildOrderEmail(Order order){
        StringBuilder s = new StringBuilder();
        s.append("Hi "). append(order.getUser().getName()).append(",\n\n");
        s.append("Your Order #").append(order.getId()).append(" has been confirmed.\n");
        s.append("Status: ").append(order.getStatus()).append("\n");
        if (order.getDeliveryAddress() != null) {
            s.append("Delivery Address: ").append(order.getDeliveryAddress()).append("\n");
        }
        if (order.getPhoneNumber() != null) {
            s.append("Phone: ").append(order.getPhoneNumber()).append("\n");
        }
        if (order.getNotes() != null) {
            s.append("Notes: ").append(order.getNotes()).append("\n");
        }
        s.append("\nItems:\n");

        for (OrderItem it : order.getItems()) {
            s.append(" - ")
                    .append(it.getFoodItem().getName())
                    .append(" x ").append(it.getQuantity())
                    .append(" @ ").append(it.getUnitPrice())
                    .append(" = ").append(it.getLineTotal())
                    .append("\n");
        }

        s.append("\nTotal: ").append(order.getTotalPrice()).append("\n");
        s.append("\nThank you for your order!");
        return s.toString();
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(PlaceOrderRequestDto dto) {
        UserInfo user = currentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CustomExceptionHandling("Cart Not Found",
                        HttpStatus.NOT_FOUND.value()));

        if(cart.getItems().isEmpty()){
            throw new CustomExceptionHandling("Cart is empty", HttpStatus.BAD_REQUEST.value());
        }

        for(CartItem line :  cart.getItems()){
            FoodItem f = line.getFoodItem();

            if(Boolean.FALSE.equals(f.getIsActive())){
                throw new CustomExceptionHandling("Item inactive: " + f.getName(),
                        HttpStatus.CONFLICT.value());

            }
            if (line.getQuantity() <= 0) {
                throw new CustomExceptionHandling("Invalid quantity for: " + f.getName(),
                        HttpStatus.BAD_REQUEST.value());
            }
            int updated = foodItemRepository.tryAllocate(f.getId(), line.getQuantity());
            if (updated == 0) {
                throw new CustomExceptionHandling("Insufficient stock: " + f.getName(),
                        HttpStatus.CONFLICT.value());
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);

        if(dto != null){
            order.setNotes(dto.getNotes());
            order.setDeliveryAddress((dto.getDeliveryAddress()));
            order.setPhoneNumber(dto.getPhoneNumber());
        }

        double total = 0.0;
        for(CartItem item : cart.getItems()){
            OrderItem o = OrderMapper.toEntity(item.getFoodItem(), item.getQuantity(), order);
            order.getItems().add(o);
            total += o.getLineTotal();
        }
        order.setTotalPrice(total);


            Order saved = orderRepository.save(order);

            cart.getItems().clear();
            cart.setTotalPrice(0.0);

            cartRepository.save(cart);

            String subject = "Order #" +saved.getId() +" confirmed";
            String message = buildOrderEmail(saved);
            emailService.sendMail(user,subject,message);
            return OrderMapper.toDto(saved);

    }

    @Override
    @Transactional
    public OrderResponseDto getOrder(Integer id) {
        UserInfo user = currentUser();
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionHandling("Order not found: " + id, HttpStatus.NOT_FOUND.value()));
        if (!o.getUser().getId().equals(user.getId())) {
            throw new CustomExceptionHandling("Forbidden", HttpStatus.FORBIDDEN.value());
        }
        return OrderMapper.toDto(o);
    }

    @Override
    public List<OrderResponseDto> myOrders() {
        UserInfo user = currentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional

    public OrderResponseDto cancelOrder(Integer id) {
        UserInfo user = currentUser();
        Order o =orderRepository.findById(id)
                .orElseThrow(()-> new CustomExceptionHandling("Order not found: " + id,
                        HttpStatus.NOT_FOUND.value()));

        if(!o.getUser().getId().equals(user.getId())){
            throw new CustomExceptionHandling("Forbidden",
                    HttpStatus.FORBIDDEN.value());
        }
        if(o.getStatus() == OrderStatus.CANCELLED){
            return OrderMapper.toDto(o);
        }

        for(OrderItem item : o.getItems()){
            foodItemRepository.addBack(item.getFoodItem().getId(), item.getQuantity());
        }

        o.setStatus(OrderStatus.CANCELLED);
        return OrderMapper.toDto(orderRepository.save(o));
    }
}
