package org.scaler.ordermanagementservice.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.exceptions.OrderStatusNotValidException;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(path = "/")
    public ResponseEntity<Object> getAllOrders(){
        try {
            log.info("Received request to get all orders");
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
        }catch (Exception e){
            log.error("Failed to get list of orders please try again after sometime");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long orderId){
        try {
            log.info("Received request to get details of order : {}",orderId);
            if (!orderService.orderExsists(orderId)) {
                throw new OrderNotFoundException(orderId);
            }
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(orderId));
        } catch (OrderNotFoundException e){
            log.error("Order not found with order id : {}",orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            log.error("Failed to get information of order : {}, Please try again after sometime",orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(path = "/order")
    public ResponseEntity<Object> saveOrder(@RequestBody OrderInfoVo orderInfoVo) {
        try {
            log.info("Received request to add a new order");
            if (!orderService.isValidOrderStatus(orderInfoVo.getOrderStatus())){
                throw new OrderStatusNotValidException();
            }
            orderService.saveOrder(orderInfoVo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (JsonProcessingException e) {
            log.error("Failed to add order due to JSON processing exception", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Failed to create order due to JSON processing exception");
        } catch (Exception e) {
            log.error("Failed to add order, please try again after sometime", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add order, please try again after sometime");
        }
    }

    @DeleteMapping(path = "/{orderId}")
    public ResponseEntity<Object> deleteOrderById(@PathVariable Long orderId){
        try {
            log.info("Received request to delete order : {}", orderId);
            if (!orderService.orderExsists(orderId)) {
                throw new OrderNotFoundException(orderId);
            }
            orderService.deleteByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted order");
        } catch (OrderNotFoundException e){
            log.error("Order not found with orderId : {}",orderId,e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        } catch (Exception e){
            log.error("Failed to delete order. Please try again after some time",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
