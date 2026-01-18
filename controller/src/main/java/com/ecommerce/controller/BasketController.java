package com.ecommerce.controller;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.AddBasketDto;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final IBasketService basketService;

    public BasketController(IBasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/addBasket")
    public String addBasket(@RequestBody AddBasketDto addBasketDto, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse<String ,String> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =    basketService.AddBasket(addBasketDto,userId.longValue());

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }

        if (result.isSuccess()) {

            return result.getSuccessData();
        }
        return result.getErrorData();
    }

    @PostMapping("/getBasket")
    public ResponseEntity<?> getBasket(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse< String,List<AddBasketDto>> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =    basketService.GetBasket(userId.longValue());

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }

        if (result.isSuccess()) {

            return ResponseEntity.ok(result.getSuccessData()) ;
        }


        return ResponseEntity.status(result.getStatusCode()).body(result.getErrorData());
    }
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteFromBasket(@PathVariable long itemId, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse< String,String> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =    basketService.DeleteFromBasket(itemId,userId.longValue());

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }

        if (result.isSuccess()) {

            return ResponseEntity.ok(result.getSuccessData()) ;
        }


        return ResponseEntity.status(result.getStatusCode()).body(result.getErrorData());
    }
}
