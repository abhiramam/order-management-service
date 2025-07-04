package org.scaler.ordermanagementservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scaler.ordermanagementservice.dtos.OrderRequestDto;
import org.scaler.ordermanagementservice.dtos.OrderResponseDto;
import org.scaler.ordermanagementservice.exceptions.OrderNotFoundException;
import org.scaler.ordermanagementservice.feign.FeignInterface;
import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.scaler.ordermanagementservice.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FeignInterface feignInterface;

    @InjectMocks
    private OrderService orderService;

    private OrderInfoVo orderInfoVo1;
    private OrderInfoVo orderInfoVo2;
    private OrderRequestDto orderRequestDto;
    private OrderResponseDto orderResponseDto;

    @BeforeEach
    void setUp() {
        orderInfoVo1 = new OrderInfoVo();
        orderInfoVo1.setOrderId(1L);
        orderInfoVo1.setCustomerName("Customer 1");
        orderInfoVo1.setBookId(101L);
        orderInfoVo1.setOrderStatus("NEW");

        orderInfoVo2 = new OrderInfoVo();
        orderInfoVo2.setOrderId(2L);
        orderInfoVo2.setCustomerName("Customer 2");
        orderInfoVo2.setBookId(102L);
        orderInfoVo2.setOrderStatus("PROCESSING");

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerName("Test Customer");
        orderRequestDto.setBookId(100L);
        orderRequestDto.setOrderStatus("NEW");

        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(1L);
        orderResponseDto.setCustomerName(orderRequestDto.getCustomerName());
        orderResponseDto.setBookId(orderRequestDto.getBookId());
        orderResponseDto.setOrderStatus(orderRequestDto.getOrderStatus());
    }

    @Test
    void getAllOrders_shouldReturnListOfOrderResponseDtos() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(orderInfoVo1, orderInfoVo2));

        List<OrderResponseDto> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orderInfoVo1.getCustomerName(), result.get(0).getCustomerName());
        assertEquals(orderInfoVo2.getCustomerName(), result.get(1).getCustomerName());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_whenOrderExists_shouldReturnOrderResponseDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderInfoVo1));

        Optional<OrderResponseDto> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(orderInfoVo1.getCustomerName(), result.get().getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderById_whenOrderDoesNotExist_shouldReturnEmptyOptional() {
        when(orderRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<OrderResponseDto> result = orderService.getOrderById(3L);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(3L);
    }

    @Test
    void saveOrder_shouldSaveAndReturnOrderResponseDto_feignCallSucceeds() throws Exception {
        OrderInfoVo savedOrderVo = new OrderInfoVo();
        savedOrderVo.setOrderId(1L); // Simulate ID generation
        savedOrderVo.setCustomerName(orderRequestDto.getCustomerName());
        savedOrderVo.setBookId(orderRequestDto.getBookId());
        savedOrderVo.setOrderStatus(orderRequestDto.getOrderStatus());

        when(orderRepository.save(any(OrderInfoVo.class))).thenReturn(savedOrderVo);
        when(feignInterface.getBookById(anyLong())).thenReturn(ResponseEntity.ok().build()); // Simulate successful Feign call

        OrderResponseDto result = orderService.saveOrder(orderRequestDto);

        assertNotNull(result);
        assertEquals(savedOrderVo.getOrderId(), result.getOrderId());
        assertEquals(orderRequestDto.getCustomerName(), result.getCustomerName());
        verify(orderRepository, times(1)).save(any(OrderInfoVo.class));
        verify(feignInterface, times(1)).getBookById(orderRequestDto.getBookId());
    }

    @Test
    void saveOrder_shouldSaveAndReturnOrderResponseDto_feignCallFails() throws Exception {
        OrderInfoVo savedOrderVo = new OrderInfoVo();
        savedOrderVo.setOrderId(1L);
        savedOrderVo.setCustomerName(orderRequestDto.getCustomerName());
        savedOrderVo.setBookId(orderRequestDto.getBookId());
        savedOrderVo.setOrderStatus(orderRequestDto.getOrderStatus());

        when(orderRepository.save(any(OrderInfoVo.class))).thenReturn(savedOrderVo);
        when(feignInterface.getBookById(anyLong())).thenThrow(new RuntimeException("Feign client error")); // Simulate Feign call failure

        OrderResponseDto result = orderService.saveOrder(orderRequestDto);

        assertNotNull(result);
        assertEquals(savedOrderVo.getOrderId(), result.getOrderId());
        assertEquals(orderRequestDto.getCustomerName(), result.getCustomerName());
        verify(orderRepository, times(1)).save(any(OrderInfoVo.class));
        verify(feignInterface, times(1)).getBookById(orderRequestDto.getBookId());
        // We expect a warning log in this case, which is harder to verify directly without log capture setup
    }


    @Test
    void deleteByOrderId_whenOrderExists_shouldDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        assertDoesNotThrow(() -> orderService.deleteByOrderId(1L));

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByOrderId_whenOrderDoesNotExist_shouldThrowOrderNotFoundException() {
        when(orderRepository.existsById(3L)).thenReturn(false);

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteByOrderId(3L);
        });

        assertEquals("Order not found with ID: 3", exception.getMessage());
        verify(orderRepository, times(1)).existsById(3L);
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void orderExists_shouldReturnTrue_whenOrderExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        assertTrue(orderService.orderExists(1L));
        verify(orderRepository, times(1)).existsById(1L);
    }

    @Test
    void orderExists_shouldReturnFalse_whenOrderDoesNotExist() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        assertFalse(orderService.orderExists(1L));
        verify(orderRepository, times(1)).existsById(1L);
    }

    @Test
    void isValidOrderStatus_withValidStatus_shouldReturnTrue() {
        assertTrue(orderService.isValidOrderStatus("NEW"));
        assertTrue(orderService.isValidOrderStatus("PROCESSING"));
        assertTrue(orderService.isValidOrderStatus("COMPLETED"));
        assertTrue(orderService.isValidOrderStatus("CANCELLED"));
    }

    @Test
    void isValidOrderStatus_withInvalidStatus_shouldReturnFalse() {
        assertFalse(orderService.isValidOrderStatus("INVALID_STATUS"));
        assertFalse(orderService.isValidOrderStatus(""));
        assertFalse(orderService.isValidOrderStatus(null));
    }
}
