package om.ecommerce.repository.implementations;

import com.ecommerce.domain.entity.QOrderEntity;
import com.ecommerce.domain.entity.QOrderItemEntity;
import com.ecommerce.domain.entity.QProductEntity;
import com.ecommerce.domain.model.FilterProductModel;
import com.ecommerce.domain.model.FilteredProductModel;
import com.ecommerce.repository.interfaces.IProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;


public class ProductRepositoryCustom implements IProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustom(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FilteredProductModel> filterProducts(FilterProductModel filter) {
        QProductEntity product = QProductEntity.productEntity;
        QOrderItemEntity orderItem = QOrderItemEntity.orderItemEntity;
        QOrderEntity order = QOrderEntity.orderEntity;

        BooleanExpression predicate = product.name.like("%" + filter.getName() + "%");

        if (filter.getMinPrice() > 0) {
            predicate = predicate.and(product.price.goe(filter.getMinPrice()));
        }

        if (filter.getMaxPrice() > 0) {
            predicate = predicate.and(product.price.loe(filter.getMaxPrice()));
        }

        return queryFactory
                .select(Projections.constructor(
                        FilteredProductModel.class,
                        product.name,
                        orderItem.quantity,
                        orderItem.id,
                        product.price
                ))
                .from(order)
                .join(orderItem).on(orderItem.orderId.eq(order.id))
                .join(product).on(product.id.eq(orderItem.productId))
                .where(predicate)
                .fetch();
    }
}
