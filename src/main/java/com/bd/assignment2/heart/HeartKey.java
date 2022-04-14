package com.bd.assignment2.heart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HeartKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "game_id")
    private Long gameId;
}
