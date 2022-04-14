package com.bd.assignment2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResDto {
    private String accessToken;
    private String refreshToken;
}
