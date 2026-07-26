package com.ecommerce.controller;

import com.ecommerce.service.dto.LoginRequestDto;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.dto.UserLovDto;
import com.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static String cookieName = "jwt";
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
            ResponseCookie cookie = ResponseCookie.from(cookieName, result.getSuccessData())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofHours(4))
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(result.getSuccessData());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletResponse response) {
       return  ResponseEntity.ok("test");
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
         auth.setAuthenticated(false);
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logout Successful");

    }
    @GetMapping("/isAdmin")
    public boolean isAdmin(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getPrincipal() instanceof Long userId ){
           return userService.isAdmin(userId);

        }
        return false;

    }
    @GetMapping("/getUserLov")
    public List<UserLovDto> getUserLov(HttpServletResponse response) {
        return userService.getUserLov();

    }
}
