package org.scaler.ordermanagementservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotBlank(message = "Order status cannot be blank")
    @Pattern(regexp = "NEW|PROCESSING|COMPLETED|CANCELLED", message = "Order status must be one of: NEW, PROCESSING, COMPLETED, CANCELLED")
    private String orderStatus;
}
