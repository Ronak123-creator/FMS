package com.backend.foodproject.dto.order;

import com.backend.foodproject.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateOrderStatusDto {

    private OrderStatus status;
    private boolean restockOnCancel;
    private String reason;
}
