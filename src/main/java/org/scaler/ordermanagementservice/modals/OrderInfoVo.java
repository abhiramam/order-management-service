package org.scaler.ordermanagementservice.modals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.scaler.ordermanagementservice.enums.OrderStatus;

@Getter
@Setter
@Entity
public class OrderInfoVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private Long customerId;

    private OrderStatus orderStatus;


}
