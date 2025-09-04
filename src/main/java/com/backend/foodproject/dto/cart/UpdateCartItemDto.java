package com.backend.foodproject.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemDto {
    @NotNull
    private Integer foodItemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
