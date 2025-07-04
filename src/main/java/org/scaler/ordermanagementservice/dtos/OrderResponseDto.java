package org.scaler.ordermanagementservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String customerName;
    private Long bookId;
    private String orderStatus;
}
