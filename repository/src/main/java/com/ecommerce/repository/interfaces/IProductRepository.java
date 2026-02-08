package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.entity.ProductEntity;
import com.ecommerce.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

}
