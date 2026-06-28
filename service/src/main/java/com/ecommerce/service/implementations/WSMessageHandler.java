package com.ecommerce.service.implementations;

import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IWSMessageHandler;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Service
public class WSMessageHandler implements IWSMessageHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private RedisTemplate redisTemplate;
    private IJwtHelper jwtHelper;
    public WSMessageHandler(SimpMessagingTemplate simpMessagingTemplate, RedisTemplate redisTemplate,IJwtHelper jwtHelper){
        messagingTemplate = simpMessagingTemplate;
        this.redisTemplate = redisTemplate;
        this.jwtHelper =jwtHelper;

    }

    @Override
    public void SendMessageToUser(long userId, String message) {
        var redisKey =  CustomUtil.GetRedisBasketKeyForUser(userId);
        var wsSessionId =    redisTemplate.opsForValue().get(redisKey);
        messagingTemplate.convertAndSendToUser(wsSessionId.toString(),"/queue/test",message);
    }

}
