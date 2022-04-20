package com.bd.assignment2.config.stomp;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.config.websocket.Fixes;
import com.bd.assignment2.project.Project;
import com.bd.assignment2.project.ProjectRepository;
import com.bd.assignment2.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final JwtService jwtService;
    private final ProjectRepository projectRepository;

//    private final SimpMessageSendingOperations simpMessageSendingOperations;

    //projectId, code
    static final Map<String, String> projects = new ConcurrentHashMap<>();

    @MessageMapping("/edit")
    public void message(Fixes message, @Header("X-AUTH-TOKEN") String jwt) {
        if (jwtService.isValid(jwt)) {
            updateCache(message, jwt);
        }
//        simpMessageSendingOperations.convertAndSend("/sub/project/"+message.getProjectId(), message);
    }

    private void updateCache(Fixes fixes, String jwt) {
        //토큰에서 유저정보 꺼냄
        User user = jwtService.getUserFromJwtForStomp(jwt);
        Project project = projectRepository.findById(Long.parseLong(fixes.getProjectId()))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다"));
        //유저가 프로젝트의 주인이 맞음
        if (user.equals(project.getUser())) {
            //캐시 메모리에 project가 있으면 수정
            if (projects.containsKey(fixes.getProjectId())) {
                projects.replace(fixes.getProjectId(), fixes.getCode());
            } else {
                projects.put(fixes.getProjectId(), fixes.getCode());
            }
        }
    }
}
