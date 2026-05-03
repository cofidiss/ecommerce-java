package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.entity.OrderItemEntity;
import com.ecommerce.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
