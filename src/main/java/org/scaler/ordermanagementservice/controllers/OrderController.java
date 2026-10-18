package org.scaler.ordermanagementservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.dtos.OrderRequestDto;
import org.scaler.ordermanagementservice.dtos.OrderResponseDto;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        log.info("Received request to get all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String orderId) {
        log.info("Received request to get details of order: {}", orderId);
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> saveOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        log.info("Received request to add a new order for customer: {}", orderRequestDto.getCustomerName());
        OrderResponseDto savedOrder = orderService.saveOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable String orderId) {
        log.info("Received request to delete order: {}", orderId);
        orderService.deleteByOrderId(orderId);
        return ResponseEntity.noContent().build();
    }
}
