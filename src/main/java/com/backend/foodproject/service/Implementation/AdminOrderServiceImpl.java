package com.backend.foodproject.service.Implementation;

import com.backend.foodproject.dto.order.AdminUpdateOrderStatusDto;
import com.backend.foodproject.dto.order.OrderResponseDto;
import com.backend.foodproject.entity.Order;
import com.backend.foodproject.entity.OrderItem;
import com.backend.foodproject.enums.OrderStatus;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.OrderMapper;
import com.backend.foodproject.repository.FoodItemRepository;
import com.backend.foodproject.repository.OrderRepository;
import com.backend.foodproject.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final FoodItemRepository foodItemRepository;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);
    static {
        ALLOWED.put(OrderStatus.PENDING,   EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED));
        ALLOWED.put(OrderStatus.CONFIRMED, EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED));
        ALLOWED.put(OrderStatus.PREPARING, EnumSet.of(OrderStatus.DISPATCHED, OrderStatus.CANCELLED));
        ALLOWED.put(OrderStatus.DISPATCHED,EnumSet.of(OrderStatus.DELIVERED));
        ALLOWED.put(OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class));
        ALLOWED.put(OrderStatus.CANCELLED, EnumSet.noneOf(OrderStatus.class));
    }

    private void assertAllowedTransition(OrderStatus from, OrderStatus to) {
        var allowed = ALLOWED.getOrDefault(from, EnumSet.noneOf(OrderStatus.class));
        if (!allowed.contains(to)) {
            throw new CustomExceptionHandling(
                    "Illegal status change: " + from + " -> " + to,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    @Override
    public List<OrderResponseDto> getAllOrder() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Page<OrderResponseDto> search(OrderStatus status, Instant from, Instant to, String q,int page, int size) {
        String term = (q == null || q.isBlank()) ? null : q.trim().toLowerCase();

        Instant fromN = (from == null) ? Instant.EPOCH : from;
        Instant toN   = (to   == null) ? Instant.parse("9999-12-31T23:59:59Z") : to;

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.adminSearch(status,fromN, toN, term, pageable)
                .map(OrderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto get(Integer id) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionHandling("Order not found: " + id,
                        HttpStatus.NOT_FOUND.value()));
        return OrderMapper.toDto(o);
    }

    @Override
    public OrderResponseDto updateStatus(Integer id, AdminUpdateOrderStatusDto dto) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionHandling("Order not found: " + id,
                        HttpStatus.NOT_FOUND.value()));

        var target = dto.getStatus();
        if(target == null){
            throw new CustomExceptionHandling("Target status is required",
                    HttpStatus.BAD_REQUEST.value());
        }

        if(target == OrderStatus.CANCELLED){
            return cancel(id, dto.getReason(), dto.isRestockOnCancel());
        }

        assertAllowedTransition(o.getStatus(), target);
        o.setStatus(target);

        if (dto.getReason() != null && !dto.getReason().isBlank()) {
            String prefix = (o.getNotes() == null || o.getNotes().isBlank()) ? "" : (o.getNotes() + " | ");
            o.setNotes(prefix + "[ADMIN STATUS] " + dto.getReason());
        }

        return OrderMapper.toDto(orderRepository.save(o));
    }

    @Override
    @Transactional
    public OrderResponseDto cancel(Integer id, String reason, boolean restock) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionHandling("Order not found: " + id, HttpStatus.NOT_FOUND.value()));

        if (o.getStatus() == OrderStatus.CANCELLED) {
            return OrderMapper.toDto(o);
        }
        assertAllowedTransition(o.getStatus(),OrderStatus.CANCELLED);

        if(restock){
            for(OrderItem it: o.getItems()){
                foodItemRepository.addBack(it.getFoodItem().getId(), it.getQuantity());
            }
        }
        o.setStatus(OrderStatus.CANCELLED);
        if(reason!=null && !reason.isBlank()){
            String prefix = (o.getNotes() == null || o.getNotes().isBlank()) ? "" : (o.getNotes() + " | ");
            o.setNotes(prefix + "[ADMIN CANCEL]" + reason);
        }
        return OrderMapper.toDto(orderRepository.save(o));
    }
}
