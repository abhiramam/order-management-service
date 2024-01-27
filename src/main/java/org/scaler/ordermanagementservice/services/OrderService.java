package org.scaler.ordermanagementservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.exceptions.OrderStatusNotValidException;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {
    
    @Autowired
    OrderRepository orderRepository;

    private boolean orderExsists(Long orderId){
        return orderRepository.existsByOrderId(orderId);
    }

    public List<OrderInfoVo> getAllOrders(){
        return orderRepository.findAll();
    }

    public OrderInfoVo getOrderById(Long orderId){
        if (orderExsists(orderId)) {
            return orderRepository.findAllByorderId(orderId);
        } else {
            throw new OrderNotFoundException(orderId);
        }

    }

    public String saveOrder(OrderInfoVo orderInfoVo) throws JsonProcessingException, JsonMappingException {
        if (isValidOrderStatus(orderInfoVo.getOrderStatus())){
            orderRepository.save(orderInfoVo);
            return "Successfully added order";
        } else {
            log.error("Not a valid Order status");
            return "";
        }
    }

    public String deleteOrderById(Long orderId){
        if (orderExsists(orderId)){
            orderRepository.deleteByorderId(orderId);
            return "Successfully deleted order";
        }else {
            throw new OrderNotFoundException(orderId);
        }
    }

    public boolean isValidOrderStatus(String orderStatus) {
        return orderStatus != null && (orderStatus.equals("NEW") || orderStatus.equals("PROCESSING")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("CANCELLED"));
    }

}
