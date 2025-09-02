package com.backend.foodproject.dto.foodDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateDto {
    private Integer foodId;
    private Integer quantityChange;
}
