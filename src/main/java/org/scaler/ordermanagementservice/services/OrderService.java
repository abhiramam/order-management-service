package org.scaler.ordermanagementservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public boolean orderExsists(Long orderId){
        return orderRepository.existsById(orderId);
    }

    public List<OrderInfoVo> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<OrderInfoVo> getOrderById(Long orderId){
            return orderRepository.findById(orderId);
    }

    public void saveOrder(OrderInfoVo orderInfoVo) throws JsonProcessingException {
        orderRepository.save(orderInfoVo);
    }

    @Transactional
    public void deleteByOrderId(Long orderId){
        if (orderExsists(orderId)){
            orderRepository.deleteById(orderId);
        }
    }

    public boolean isValidOrderStatus(String orderStatus) {
        return orderStatus != null && (orderStatus.equals("NEW") || orderStatus.equals("PROCESSING")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("CANCELLED"));
    }

}
