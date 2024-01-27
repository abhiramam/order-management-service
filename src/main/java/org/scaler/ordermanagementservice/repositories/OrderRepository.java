package org.scaler.ordermanagementservice.repositories;

import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfoVo,String> {

    OrderInfoVo findAllByorderId(Long orderId);

    void deleteByOrderId(Long orderId);

    boolean existsAllByOrderId(Long orderId);
}
