package com.bd.assignment2.config.websocket;

import com.bd.assignment2.project.ProjectService;
import com.bd.assignment2.project.dto.UpdateProjectReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ProjectService projectService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, UpdateProjectReqDto> projects = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        System.out.println("sessionId = " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Fixes message = Utils.getObject(textMessage.getPayload());
        if (sessions.containsKey(session.getId())) {
            projects.replace(session.getId(), message.getUpdateProjectReqDto());
        } else {
            projects.put(session.getId(), message.getUpdateProjectReqDto());
            sessions.put(session.getId(), session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UpdateProjectReqDto updateProjectReqDto = projects.get(session.getId());
        projectService.update(updateProjectReqDto.getProjectId(), updateProjectReqDto.getCode());
        sessions.remove(session.getId());
    }
}
