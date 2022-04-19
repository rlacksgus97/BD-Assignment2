package com.bd.assignment2.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProjectReqDto {
    private Long projectId;
    private String code;
}
