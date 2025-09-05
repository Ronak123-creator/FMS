package com.backend.foodproject.repository;

import com.backend.foodproject.entity.Order;
import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreatedAtDesc(UserInfo user);

    @EntityGraph(attributePaths = {"items", "items.foodItem", "user"})
    @Query("""
   select o from Order o
   join o.user u
   left join o.items i
   left join i.foodItem fi
   where (:status is null or o.status = :status)
     and o.createdAt >= :from
     and o.createdAt <  :to
     and (
          :q is null or
          lower(coalesce(o.deliveryAddress, '')) like concat('%', :q, '%') or
          lower(coalesce(o.notes,           '')) like concat('%', :q, '%') or
          lower(coalesce(o.phoneNumber,     '')) like concat('%', :q, '%') or
          lower(coalesce(u.name,            '')) like concat('%', :q, '%')
     )
   order by o.createdAt desc
""")
    Page<Order> adminSearch(
            @Param("status") OrderStatus status,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("q") String q,
            Pageable pageable
    );



}
