package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.OrderEntity;
import com.ecommerce.domain.entity.OrderItemEntity;
import com.ecommerce.domain.entity.ProductEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.repository.interfaces.IOrderItemRepository;
import com.ecommerce.repository.interfaces.IOrderRepository;
import com.ecommerce.repository.interfaces.IProductRepository;
import com.ecommerce.service.config.RabbitConfig;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
        private IProductRepository productRepository;
    private IOrderItemRepository orderItemRepository;
    private IOrderRepository orderRepository;
    private RedisTemplate redisTemplate;
    private IBasketService basketService;
    private RabbitTemplate rabbitTemplate;
    public OrderService(RedisTemplate redisTemplate,IBasketService basketService,IProductRepository productRepository,
                        IOrderItemRepository orderItemRepository, IOrderRepository orderRepository,
                        RabbitTemplate rabbitTemplate){
this.redisTemplate = redisTemplate;
this.basketService = basketService;
this.productRepository = productRepository;
this.orderItemRepository = orderItemRepository;
this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
}

// todo transaction yönetimi yap
    @Transactional
    @Override
    public GenericApiResponse<String, String> CreateOrder(long userId) {
        var apiResponse = new GenericApiResponse<String, String >();
      var basketApiResponse =   basketService.GetBasket(userId);
      if (!basketApiResponse.isSuccess()){
          return  apiResponse;
      }

        var orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderRepository.saveAndFlush(orderEntity);

        var orderItemEntityEntityList = new ArrayList<OrderItemEntity>();
var toUpdateEntityList = new ArrayList<ProductEntity>();
      var basketItemList =  basketApiResponse.getSuccessData();
        for (var basketItem : basketItemList) {
          var productEntityInDbOpt =  productRepository.findById(basketItem.getProductId());
          ProductEntity productEntityInDb = null;
          if (!productEntityInDbOpt.isPresent()){
              apiResponse.setErrorData("""
                      productId: %d is not available
                      """.formatted(basketItem.getProductId()));
          } else if (productEntityInDbOpt.get().getQuantity() < basketItem.getQuantity()) {
              productEntityInDb = productEntityInDbOpt.get();
              apiResponse.setErrorData("""
                      productName: %s can not be ordered more than %f
                      """.formatted(productEntityInDb.getName(),productEntityInDb.getQuantity()));
              
          }
          if(!apiResponse.isSuccess())
              return apiResponse;
            productEntityInDb.setQuantity(productEntityInDb.getQuantity() - basketItem.getQuantity() );
            var orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductId(basketItem.getProductId());
            orderItemEntity.setQuantity(basketItem.getQuantity());
            orderItemEntityEntityList.add(orderItemEntity);
            orderItemEntity.setOrderId(orderEntity.getId());
            toUpdateEntityList.add(productEntityInDb);

        }
        productRepository.saveAllAndFlush(toUpdateEntityList);
       orderItemRepository.saveAllAndFlush(orderItemEntityEntityList);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        rabbitTemplate.convertAndSend(RabbitConfig.OrdersEXCHANGE, orderEntity.getId());
                    }
                }
        );


        return null;
    }
}
