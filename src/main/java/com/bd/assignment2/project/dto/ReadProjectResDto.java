package com.bd.assignment2.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadProjectResDto {
    private String title;
    private String code;
}
