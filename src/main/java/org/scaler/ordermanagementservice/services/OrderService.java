package org.scaler.ordermanagementservice.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scaler.ordermanagementservice.enums.OrderStatus;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    OrderRepository orderRepository;

    private boolean orderExsists(Long orderId){
        return orderRepository.existsByOrderId(orderId);
    }

    public List<OrderInfoVo> getAllOrders(){
        return orderRepository.findAll();
    }

    public OrderInfoVo getOrderById(Long orderId){
        if (orderExsists(orderId)) {
            return orderRepository.findAllById(orderId);
        } else {
            throw new OrderNotFoundException(orderId);
        }

    }

    public String saveOrder(String orderInfoVo) throws JsonProcessingException,JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderInfoVo orderInfoVo1 = objectMapper.readValue(orderInfoVo,OrderInfoVo.class);
        if (isValidOrderStatus(String.valueOf(orderInfoVo1.getOrderStatus()))){
            orderRepository.save(orderInfoVo1);
            return "Successfully added order";
        } else {
            return "Not a valid order status";
        }
    }

    public String deleteOrderById(Long orderId){
        if (orderExsists(orderId)){
            orderRepository.deleteById(orderId);
            return "Successfully deleted order";
        }else {
            throw new OrderNotFoundException(orderId);
        }
    }

    private boolean isValidOrderStatus(String orderStatus) {
        return orderStatus != null && (orderStatus.equals("New") || orderStatus.equals("Processing")
                || orderStatus.equals("Completed") || orderStatus.equals("Cancelled"));
    }

}
