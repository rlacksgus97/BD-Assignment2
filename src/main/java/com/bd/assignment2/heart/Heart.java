package com.bd.assignment2.heart;

import com.bd.assignment2.game.Game;
import com.bd.assignment2.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Heart {
    @EmbeddedId
    private HeartKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("game_id")
    @JoinColumn(name = "game_id")
    private Game game;
}