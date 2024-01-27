package org.scaler.ordermanagementservice.exceptions;

public class OrderStatusNotValidException extends RuntimeException{

    public OrderStatusNotValidException(){
        super("Order Status Not valid");
    }
}
