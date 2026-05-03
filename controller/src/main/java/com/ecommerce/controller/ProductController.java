package com.ecommerce.controller;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.FilterProductDto;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.interfaces.IOrderService;
import com.ecommerce.service.interfaces.IProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/filterProducts")
    public ResponseEntity<?> filterProduct(@RequestBody FilterProductDto filterProductDto, HttpServletResponse response) {
      var genericResponse =  productService.GetFilteredProductDtoList(filterProductDto);
      if (genericResponse.isSuccess())
          return ResponseEntity.ok(genericResponse.getSuccessData());

       return ResponseEntity.status(genericResponse.getStatusCode()).body(genericResponse.getErrorData());
    }
    @GetMapping("/getCategoryLov")
    public ResponseEntity<?> getCategoryLov(HttpServletResponse response) {
        var genericResponse =  productService.getCategoryLov();
        if (genericResponse.isSuccess())
            return ResponseEntity.ok(genericResponse.getSuccessData());

        return ResponseEntity.status(genericResponse.getStatusCode()).body(genericResponse.getErrorData());
    }


}
