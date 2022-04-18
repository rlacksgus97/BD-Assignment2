package com.bd.assignment2.game;

import com.bd.assignment2.game.dto.PublishGameReqDto;
import com.bd.assignment2.heart.Heart;
import com.bd.assignment2.project.Project;
import com.bd.assignment2.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String code;

    private Long hit;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "game")
    private Project project;

    @OneToMany(mappedBy = "game")
    private List<Heart> hearts;

    public void update(PublishGameReqDto publishGameReqDto) {
        this.title = publishGameReqDto.getTitle();
        this.code = publishGameReqDto.getCode();
    }
}
