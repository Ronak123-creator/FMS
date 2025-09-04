package com.backend.foodproject.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCart {
    @NonNull
    private Integer foodItemId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
