package com.bd.assignment2.game.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadGameResDto {
    private String title;
    private String code;
    private String userName;
}
