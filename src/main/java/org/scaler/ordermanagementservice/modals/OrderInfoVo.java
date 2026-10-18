package org.scaler.ordermanagementservice.modals;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "orders")
public class OrderInfoVo {

    @Id
    private String orderId;

    private String customerName;

    private Long bookId;

    private String orderStatus;
}
