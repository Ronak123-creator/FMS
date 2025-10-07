package com.backend.foodproject.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LowStockItemDto {
    private Integer id;
    private String name;
    private String categoryName;
    private Integer quantity;
    private Instant updatedAt;
}
