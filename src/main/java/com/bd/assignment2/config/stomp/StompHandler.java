package com.bd.assignment2.config.stomp;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.config.websocket.Fixes;
import com.bd.assignment2.project.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtService jwtService;
    private final ProjectService projectService;

    //sessionId, projectId
    static final Map<String, String> sessions = new ConcurrentHashMap<>();

    @SneakyThrows
    @Transactional
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("message:" + message);
        System.out.println("헤더 : " + message.getHeaders());
        System.out.println("토큰 : " + accessor.getNativeHeader("X-AUTH-TOKEN"));
        System.out.println("accessor.getCommand() = " + accessor.getCommand());
        System.out.println("");

        switch (accessor.getCommand()) {
            case CONNECT:
                //토큰 확인
                jwtService.isValid(Objects.requireNonNull(accessor.getFirstNativeHeader("X-AUTH-TOKEN")));

                //sessions에 등록
                sessions.put(accessor.getSessionId(), "-1");
                break;

            case SEND:
                //토큰 확인, 아니면 MessageController에서 @Header로 검사해도 됨

                //sessions에 등록
                if (sessions.containsKey(accessor.getSessionId())) {
                    ObjectMapper mapper = new ObjectMapper();
                    Fixes fixes = mapper.readValue(message.getPayload().toString(), Fixes.class);
                    sessions.replace(accessor.getSessionId(), fixes.getProjectId());
                } else {
                    sessions.put(accessor.getSessionId(), null);
                }
                break;

            case DISCONNECT:
                //토큰 확인

                //캐시 메모리 정보 DB에 반영
                System.out.println("이거 확인점 = " + sessions.get(accessor.getSessionId()));
                if (!sessions.get(accessor.getSessionId()).equals("-1")){
                    updateDB(accessor);
                    //sessions에서 제거
                    sessions.remove(accessor.getSessionId());
                }
                break;

            default:
                break;
        }
        return message;
    }

    private void updateDB(StompHeaderAccessor accessor) {
        String projectId = sessions.get(accessor.getSessionId());
        if (MessageController.projects.containsKey(projectId)) {
            String code = MessageController.projects.get(projectId);
            projectService.update(Long.parseLong(projectId), code);
        }
    }
}