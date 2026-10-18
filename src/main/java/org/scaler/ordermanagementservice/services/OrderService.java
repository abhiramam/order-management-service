package org.scaler.ordermanagementservice.services;

import org.scaler.ordermanagementservice.dtos.OrderRequestDto;
import org.scaler.ordermanagementservice.dtos.OrderResponseDto;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean orderExists(String orderId) {
        return orderRepository.existsById(orderId);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<OrderResponseDto> getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto);
    }

    public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) {
        OrderInfoVo orderInfoVo = convertToEntity(orderRequestDto);
        OrderInfoVo savedOrder = orderRepository.save(orderInfoVo);
        return convertToDto(savedOrder);
    }

    public void deleteByOrderId(String orderId) {
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
