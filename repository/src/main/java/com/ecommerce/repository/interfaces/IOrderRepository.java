package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.entity.OrderEntity;
import com.ecommerce.domain.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
}
