package com.ecommerce.repository.implementations;

import com.ecommerce.domain.entity.QOrderEntity;
import com.ecommerce.domain.entity.QOrderItemEntity;
import com.ecommerce.domain.entity.QProductEntity;
import com.ecommerce.domain.entity.QUserEntity;
import com.ecommerce.domain.model.FilterOrderModel;
import com.ecommerce.domain.model.FilteredOrderModel;
import com.ecommerce.domain.model.OrderDetailModel;
import com.ecommerce.repository.interfaces.IOrderRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IOrderRepositoryImpl implements IOrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    public IOrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<FilteredOrderModel> customFilterOrder(FilterOrderModel filter) {

        QOrderEntity order = QOrderEntity.orderEntity;
        QOrderItemEntity orderItem = QOrderItemEntity.orderItemEntity;
        QProductEntity product = QProductEntity.productEntity;
        QUserEntity user = QUserEntity.userEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getUserId() > 0) {
            builder.and(order.userId.eq(filter.getUserId()));
        }
        BooleanBuilder havingBuilder = new BooleanBuilder();


        NumberExpression<Double> totalAmount =
                orderItem.quantity.doubleValue()
                        .multiply(product.price)
                        .sum();
        if (filter.getMaxAmount() == null || filter.getMaxAmount() == 0){
            filter.setMaxAmount(Double.MAX_VALUE);
        }
        if (filter.getMinAmount() == null || filter.getMinAmount() == 0){
            filter.setMinAmount((double) 0);
        }

            havingBuilder.and(totalAmount.between(
                    filter.getMinAmount(),
                    filter.getMaxAmount()
            ));

        return queryFactory
                .select(Projections.bean(
                        FilteredOrderModel.class,
                        user.name.as("userName"),
                        order.id.as("orderId"),
                        totalAmount.as("orderAmount")
                ))
                .from(order)
                .join(user).on(order.userId.eq(user.id))
                .join(orderItem).on(orderItem.orderId.eq(order.id))
                .join(product).on(orderItem.productId.eq(product.id))
                .where(builder)
                .groupBy(order.id,user.name)
                .having(havingBuilder).orderBy(order.id.desc())
                .fetch();
    }

    @Override
    public List<OrderDetailModel> getOrderDetailModel(Long orderId) {

        QOrderEntity order = QOrderEntity.orderEntity;
        QOrderItemEntity orderItem = QOrderItemEntity.orderItemEntity;
        QProductEntity product = QProductEntity.productEntity;
        QUserEntity user = QUserEntity.userEntity;

        return queryFactory
                .select(Projections.bean(
                        OrderDetailModel.class,
                        order.id.as("id"),
                        user.name.as("userName"),
                        product.id.as("productId"),
                        orderItem.quantity.doubleValue().as("quantity"),
                        product.price
                                .multiply(orderItem.quantity.doubleValue())
                                .as("amount")
                ))
                .from(order)
                .join(user).on(order.userId.eq(user.id))
                .join(orderItem).on(orderItem.orderId.eq(order.id))
                .join(product).on(orderItem.productId.eq(product.id))
                .where(order.id.eq(orderId))
                .fetch();


    }
}
