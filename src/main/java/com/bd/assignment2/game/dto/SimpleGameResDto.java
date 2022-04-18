package com.bd.assignment2.game.dto;

import com.bd.assignment2.game.Game;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleGameResDto {
    private String title;
    private String userName;
    private Long hit;

    public static SimpleGameResDto toDto(Game game) {
        return SimpleGameResDto.builder()
                .title(game.getTitle())
                .userName(game.getUser().getEmail())
                .hit(game.getHit())
                .build();
    }
}
