package org.scaler.ordermanagementservice.repositories;

import org.scaler.ordermanagementservice.modals.OrderInfoVo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderInfoVo, String> {

}
