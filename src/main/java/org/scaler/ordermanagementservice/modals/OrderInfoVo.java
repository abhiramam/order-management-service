package org.scaler.ordermanagementservice.modals;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** MongoDB document stored in the {@code orders} collection. */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "orders")
public class OrderInfoVo {

    @Id
    private String orderId;

    private String customerName;

    private Long bookId;

    private String orderStatus;
}
