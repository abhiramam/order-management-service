package org.scaler.ordermanagementservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.dtos.OrderRequestDto;
import org.scaler.ordermanagementservice.dtos.OrderResponseDto;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.exceptions.OrderStatusNotValidException;
import org.scaler.ordermanagementservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(path = "/")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        try {
            log.info("Received request to get all orders");
            List<OrderResponseDto> orders = orderService.getAllOrders();
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } catch (Exception e) {
            log.error("Failed to get list of orders please try again after sometime", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        try {
            log.info("Received request to get details of order : {}", orderId);
            return orderService.getOrderById(orderId)
                    .map(orderDto -> ResponseEntity.ok().body(orderDto))
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
        } catch (OrderNotFoundException e) {
            log.error("Order not found with order id : {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Failed to get information of order : {}, Please try again after sometime", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(path = "/order")
    @RateLimiter(name = "orderRateLimiter")
    public ResponseEntity<Object> saveOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        try {
            log.info("Received request to add a new order for customer: {}", orderRequestDto.getCustomerName());
            // The @Pattern annotation on DTO handles orderStatus validation format.
            // The service layer's isValidOrderStatus might be redundant now for format, but can be kept for other business logic if any.
            // For now, relying on @Valid and @Pattern.
            OrderResponseDto savedOrder = orderService.saveOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (OrderStatusNotValidException e) { // This might be less likely if @Pattern is comprehensive
            log.error("Invalid order status (custom validation): {}", orderRequestDto.getOrderStatus(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order status provided.");
        } catch (JsonProcessingException e) {
            log.error("Failed to add order due to JSON processing exception", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Failed to create order due to JSON processing exception");
        } catch (Exception e) {
            log.error("Failed to add order, please try again after sometime", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add order, please try again after sometime");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation errors: {}", errors);
        return errors;
    }

    @DeleteMapping(path = "/{orderId}")
    public ResponseEntity<Object> deleteOrderById(@PathVariable Long orderId) {
        try {
            log.info("Received request to delete order : {}", orderId);
            // The check for existence is now implicitly handled by orderService.deleteByOrderId or it throws OrderNotFoundException
            orderService.deleteByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted order with ID: " + orderId);
        } catch (OrderNotFoundException e) {
            log.error("Order not found with orderId : {}", orderId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with ID: " + orderId);
        } catch (Exception e) {
            log.error("Failed to delete order. Please try again after some time", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete order with ID: " + orderId);
        }
    }
}
