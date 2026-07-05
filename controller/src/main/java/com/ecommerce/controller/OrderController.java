package com.ecommerce.controller;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.FilterOrderDto;
import com.ecommerce.service.dto.FilteredOrderDto;
import com.ecommerce.service.dto.LoginRequestDto;
import com.ecommerce.service.interfaces.IOrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse<String ,String> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =    orderService.CreateOrder(userId);

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }
        if (result.isSuccess())
            return ResponseEntity.status(result.getStatusCode()).body(result.getSuccessData());
        return  ResponseEntity.status(result.getStatusCode()).body(result.getErrorData());

    }

    @GetMapping("/getUserOrder")
    public ResponseEntity<?> getUserOrder(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse<String, List<FilteredOrderDto>> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =  orderService.getUserOrder(userId.longValue());

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }
        if (result.isSuccess())
            return ResponseEntity.status(result.getStatusCode()).body(result.getSuccessData());
        return  ResponseEntity.status(result.getStatusCode()).body(result.getErrorData());
    }

    @GetMapping("/filterOrder")
    public ResponseEntity<?> filterOrder(FilterOrderDto filter, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        GenericApiResponse<String, List<FilteredOrderDto>> result;
        if(auth.getPrincipal() instanceof Long userId ){

            result =  orderService.filterOrder(filter);

        }
        else {

            result = new GenericApiResponse<>();
            result.setErrorData("userid is null");
        }
        if (result.isSuccess())
            return ResponseEntity.status(result.getStatusCode()).body(result.getSuccessData());
        return  ResponseEntity.status(result.getStatusCode()).body(result.getErrorData());
    }
}
