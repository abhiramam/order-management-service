package org.scaler.ordermanagementservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
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

    public boolean orderExsists(Long orderId){
        return orderRepository.existsAllByOrderId(orderId);
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

    public void saveOrder(OrderInfoVo orderInfoVo) throws JsonProcessingException {
            orderRepository.save(orderInfoVo);
    }

    @Transactional
    public void deleteByOrderId(Long orderId){
        if (orderExsists(orderId)){
            orderRepository.deleteByOrderId(orderId);
        }
    }

    public boolean isValidOrderStatus(String orderStatus) {
        return orderStatus != null && (orderStatus.equals("NEW") || orderStatus.equals("PROCESSING")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("CANCELLED"));
    }

}
