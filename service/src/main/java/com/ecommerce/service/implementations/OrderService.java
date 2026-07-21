package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.OrderEntity;
import com.ecommerce.domain.entity.OrderItemEntity;
import com.ecommerce.domain.entity.ProductEntity;
import com.ecommerce.domain.model.FilterOrderModel;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.repository.interfaces.IOrderItemRepository;
import com.ecommerce.repository.interfaces.IOrderRepository;
import com.ecommerce.repository.interfaces.IProductRepository;
import com.ecommerce.service.config.RabbitConfig;
import com.ecommerce.service.dto.FilterOrderDto;
import com.ecommerce.service.dto.FilteredOrderDto;
import com.ecommerce.service.dto.OrderDetailDto;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IOrderService;
import com.ecommerce.service.mapper.IOrderMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
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
    private IOrderMapper orderMapper;
    public OrderService(RedisTemplate redisTemplate,IBasketService basketService,IProductRepository productRepository,
                        IOrderItemRepository orderItemRepository, IOrderRepository orderRepository,
                        RabbitTemplate rabbitTemplate, IOrderMapper orderMapper){
this.redisTemplate = redisTemplate;
this.basketService = basketService;
this.productRepository = productRepository;
this.orderItemRepository = orderItemRepository;
this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.orderMapper = orderMapper;
}

// todo transaction yönetimi yap
    @Transactional
    @Override
    public GenericApiResponse<String, String> CreateOrder(long userId) {
        var apiResponse = new GenericApiResponse<String, String >();
        apiResponse.setSuccessData("order created");
      var basketApiResponse =   basketService.GetBasket(userId);
      if (!basketApiResponse.isSuccess()){
          apiResponse.setStatusCode(400);
          apiResponse.setErrorData("basket is empty");
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
          ProductEntity productEntityInDb = productEntityInDbOpt.get();
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
          if(!apiResponse.isSuccess()){
              apiResponse.setStatusCode(400);

              return apiResponse;
          }

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
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        rabbitTemplate.convertAndSend(RabbitConfig.OrdersEXCHANGE, RabbitConfig.ORDER_CREATED,orderEntity.getId());
                    }
                }
        );


        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, List<FilteredOrderDto>> getUserOrder(long userId) {

        var apiResponse = new GenericApiResponse<String, List<FilteredOrderDto>>();
var filterOrderModel = new FilterOrderModel();
filterOrderModel.setUserId(userId);
 var filteredOrderModelList = orderRepository.customFilterOrder(filterOrderModel);

 var filteredOrderDtoList = orderMapper.toFilteredOrderDtoList(filteredOrderModelList);
        apiResponse.setSuccessData(filteredOrderDtoList);
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, List<FilteredOrderDto>> filterOrder(FilterOrderDto filter) {

        var apiResponse = new GenericApiResponse<String, List<FilteredOrderDto>>();
      var filterOrderModel =  orderMapper.toFilterOrderModel(filter);

        var filteredOrderModelList = orderRepository.customFilterOrder(filterOrderModel);

        var filteredOrderDtoList = orderMapper.toFilteredOrderDtoList(filteredOrderModelList);
        apiResponse.setSuccessData(filteredOrderDtoList);
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, List<OrderDetailDto>> getOrderDetail(Long orderId) {

        var apiResponse = new GenericApiResponse<String, List<OrderDetailDto>>();
        var orderDetailModelList = orderRepository.getOrderDetailModel(orderId);

        var orderDetailDtoList = orderMapper.toOrderDetailDtoList(orderDetailModelList);
        apiResponse.setSuccessData(orderDetailDtoList);
        return apiResponse;
    }
}
