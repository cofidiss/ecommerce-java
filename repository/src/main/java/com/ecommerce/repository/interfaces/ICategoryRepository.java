package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.entity.CategoryEntity;
import com.ecommerce.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
