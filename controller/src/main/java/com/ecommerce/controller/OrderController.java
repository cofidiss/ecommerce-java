package com.ecommerce.controller;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.interfaces.IOrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/createOrder")
    public String addBasket(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse<String ,String> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =    orderService.CreateOrder(userId);

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


}
