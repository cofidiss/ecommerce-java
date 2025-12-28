package com.ecommerce.controller;

import com.ecommerce.service.dto.LoginRequestDto;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUpRequestDto signUpRequest, HttpServletResponse response) {
        var result = userService.signUp(signUpRequest);
        if (result.isSuccess()) {

            return "Signup Successful";
        }
        return "Signup Unsuccessful";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        var result = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (result.isSuccess()) {

            return ResponseEntity.ok(result.getSuccessData());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletResponse response) {
       return  ResponseEntity.ok("test");
    }

}
