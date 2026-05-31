package com.ecommerce.repository.implementations;

import com.ecommerce.domain.entity.QOrderEntity;
import com.ecommerce.domain.entity.QOrderItemEntity;
import com.ecommerce.domain.entity.QProductEntity;
import com.ecommerce.domain.model.FilterProductModel;
import com.ecommerce.domain.model.FilteredProductModel;
import com.ecommerce.repository.interfaces.IProductRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IProductRepositoryImpl implements IProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public IProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FilteredProductModel> filterProducts(FilterProductModel filter) {
        QProductEntity product = QProductEntity.productEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            builder.and(product.name.containsIgnoreCase(filter.getName()));
        }

        if (filter.getMinPrice() > 0) {
            builder.and(product.price.goe(filter.getMinPrice()));
        }

        if (filter.getMaxPrice() > 0) {
            builder.and(product.price.loe(filter.getMaxPrice()));
        }
        if (filter.getCategoryId() != null && filter.getCategoryId() != 0 ) {
            builder.and(product.categoryId.eq(filter.getCategoryId()));
        }

        return queryFactory
                .select(Projections.constructor(
                        FilteredProductModel.class,
                        product.name,
                        product.quantity,
                        product.id,
                        product.price
                ))
                .from(product)
                .where(builder)
                .orderBy(product.id.asc())
                .fetch();
    }
}
