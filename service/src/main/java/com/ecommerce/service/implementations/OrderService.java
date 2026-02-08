package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.ProductEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.repository.interfaces.IProductRepository;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
        private IProductRepository productRepository;
    private RedisTemplate redisTemplate;
    private IBasketService basketService;
    public OrderService(RedisTemplate redisTemplate,IBasketService basketService,IProductRepository productRepository){
this.redisTemplate = redisTemplate;
this.basketService = basketService;
this.productRepository = productRepository;

}


    @Override
    public GenericApiResponse<String, String> CreateOrder(long userId) {
        var apiResponse = new GenericApiResponse<String, String >();
      var basketApiResponse =   basketService.GetBasket(userId);
      if (!basketApiResponse.isSuccess()){
          return  apiResponse;
      }
var toUpdateEntityList = new ArrayList<ProductEntity>();
      var basketItemList =  basketApiResponse.getSuccessData();
        for (var basketItem : basketItemList) {
          var productEntityInDbOpt =  productRepository.findById(basketItem.getItemId());
          ProductEntity productEntityInDb = null;
          if (!productEntityInDbOpt.isPresent()){
              apiResponse.setErrorData("""
                      productId: %d is not available
                      """.formatted(basketItem.getItemId()));
          } else if (productEntityInDbOpt.get().getQuantity() < basketItem.getQuantity()) {
              productEntityInDb = productEntityInDbOpt.get();
              apiResponse.setErrorData("""
                      productName: %s can not be ordered more than %f
                      """.formatted(productEntityInDb.getName(),productEntityInDb.getQuantity()));
              
          }
          if(!apiResponse.isSuccess())
              return apiResponse;
            productEntityInDb.setQuantity(productEntityInDb.getQuantity() - basketItem.getQuantity() );
            toUpdateEntityList.add(productEntityInDb);
            // kullanım
        }
        productRepository.saveAllAndFlush(toUpdateEntityList);
        return null;
    }
}
