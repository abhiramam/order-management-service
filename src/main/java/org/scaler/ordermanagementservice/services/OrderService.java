package org.scaler.ordermanagementservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.scaler.ordermanagementservice.dtos.OrderRequestDto;
import org.scaler.ordermanagementservice.dtos.OrderResponseDto;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.feign.FeignInterface;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FeignInterface feignInterface;

    public boolean orderExists(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<OrderResponseDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto);
    }

    @Transactional
    public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) throws JsonProcessingException {
        OrderInfoVo orderInfoVo = convertToEntity(orderRequestDto);
        OrderInfoVo savedOrder = orderRepository.save(orderInfoVo);
        Long bookId = savedOrder.getBookId();
        // We'll address the Feign call's error handling and implications in a later step
        try {
            feignInterface.getBookById(bookId);
            log.info("Successfully fetched book details for bookId: {}", bookId);
        } catch (Exception e) {
            log.warn("Failed to fetch book details for bookId: {}. Order saved without book verification.", bookId, e);
            // Depending on requirements, might throw an exception or handle differently
        }
        return convertToDto(savedOrder);
    }

    @Transactional
    public void deleteByOrderId(Long orderId) {
        if (orderExists(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new OrderNotFoundException(orderId);
        }
    }

    public boolean isValidOrderStatus(String orderStatus) {
        return orderStatus != null && (orderStatus.equals("NEW") || orderStatus.equals("PROCESSING")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("CANCELLED"));
    }

    private OrderResponseDto convertToDto(OrderInfoVo orderInfoVo) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(orderInfoVo.getOrderId());
        dto.setCustomerName(orderInfoVo.getCustomerName());
        dto.setBookId(orderInfoVo.getBookId());
        dto.setOrderStatus(orderInfoVo.getOrderStatus());
        return dto;
    }

    private OrderInfoVo convertToEntity(OrderRequestDto orderRequestDto) {
        OrderInfoVo entity = new OrderInfoVo();
        entity.setCustomerName(orderRequestDto.getCustomerName());
        entity.setBookId(orderRequestDto.getBookId());
        entity.setOrderStatus(orderRequestDto.getOrderStatus());
        return entity;
    }
}
