package com.backend.foodproject.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequestDto {
    private String notes;
    private String deliveryAddress;
    private String phoneNumber;
}
