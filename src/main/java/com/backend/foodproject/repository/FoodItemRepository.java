package com.backend.foodproject.repository;

import com.backend.foodproject.dto.stock.LowStockItemDto;
import com.backend.foodproject.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    List<FoodItem> findByCategoryId(int categoryId);
    Page<FoodItem> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(
            String q1, String q2, Pageable pageable
    );

    @Modifying(flushAutomatically = true)
    @Query("""
        update FoodItem f
           set f.quantity = f.quantity - :qty
         where f.id = :id
           and f.isActive = true
           and f.quantity >= :qty
    """)
    int tryAllocate(@Param("id") Integer id, @Param("qty") int qty);

    @Modifying(flushAutomatically = true)
    @Query("""
        update FoodItem f
           set f.quantity = f.quantity + :qty
         where f.id = :id
    """)
    int addBack(@Param("id") Integer id, @Param("qty") int qty);

    @Query("""
           select f from FoodItem f
           join fetch f.category c
           where f.isActive = true and f.quantity > 0
           order by c.name asc, f.name asc
           """)
    List<FoodItem> findPublicFoods();

    @Query(
            value = """
        select new com.backend.foodproject.dto.stock.LowStockItemDto(
            f.id,
            f.name,
            f.category.name,
            f.quantity,
            f.updatedAt
        )
        from FoodItem f
        where f.isActive = true
          and f.quantity <= :threshold
        order by f.quantity asc, f.updatedAt asc
        """,
            countQuery = """
        select count(f)
        from FoodItem f
        where f.isActive = true
          and f.quantity <= :threshold
        """
    )
    Page<LowStockItemDto> findLowStock(@Param("threshold") int threshold, Pageable pageable);

    long countByIsActiveTrueAndQuantityLessThanEqual(Integer threshold);

}
