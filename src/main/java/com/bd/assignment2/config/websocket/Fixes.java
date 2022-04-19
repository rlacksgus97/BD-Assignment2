package com.bd.assignment2.config.websocket;

import com.bd.assignment2.project.dto.UpdateProjectReqDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixes {
    private String sender;
    private String projectId;
    private String code;
    private UpdateProjectReqDto updateProjectReqDto;
}
