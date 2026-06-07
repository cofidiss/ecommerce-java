package com.ecommerce.filter;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.enums.UserRole;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.UserDto;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

    private final IUserService userService;
    private final IJwtHelper jwtHelper;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthFilter(IJwtHelper jwtHelper, RedisTemplate<String, Object> redisTemplate, IUserService userService) {
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Long userId = null;
        UserDto cachedUser = null;
        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            token = Arrays.stream(cookies).filter(c -> "jwt".equals(c.getName())).map(Cookie::getValue).findFirst().orElse(null);
        }

        if (token != null && jwtHelper.validateToken(token)) {

            userId = jwtHelper.extractUserId(token); // Long kalıyor


            String redisKey = CustomUtil.GetRedisKeyForSession(userId);
            var userInRedis = redisTemplate.opsForValue().get(redisKey);
            if (userInRedis instanceof UserDto) {
                cachedUser = (UserDto) userInRedis;
            } else {
                cachedUser = userService.getUserDto(userId);
                redisTemplate.opsForValue().set(redisKey, cachedUser);

            }


            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRole.fromId((int) cachedUser.getRoleId()).name()));

            Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}