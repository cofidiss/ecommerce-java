package com.ecommerce.filter;

import com.ecommerce.domain.enums.UserRole;
import com.ecommerce.service.dto.UserDto;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private IUserService userService;
    private IJwtHelper jwtHelper;
    private RedisTemplate redisTemplate;
    public JwtAuthFilter(IJwtHelper jwtHelper, RedisTemplate redisTemplate,IUserService userService){
        this. userService = userService;
this.jwtHelper = jwtHelper;
this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            var tokenValid = jwtHelper.validateToken(token);
            if(tokenValid){
                var userId = jwtHelper.extractUserId(token);
             var cachedUserEntity =    redisTemplate.opsForValue().get(String.valueOf(userId));

if(!(cachedUserEntity  instanceof   UserDto userDto)){

   var  userDto = userService.getUserDto(userId);
    redisTemplate.opsForValue().set(String.valueOf(userId),userDto);
    cachedUserEntity = userDto;
}
           var roleId =     ((UserDto) cachedUserEntity).getRoleId();


                List<GrantedAuthority> authorities =
                        List.of(
                                new SimpleGrantedAuthority(
                                        UserRole.fromId((int) roleId).name()
                                )
                        );

                Authentication auth =
                        new UsernamePasswordAuthenticationToken(
                                userId, null, authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(request, response);
    }
}