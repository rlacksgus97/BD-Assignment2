package com.bd.assignment2.user;

import com.bd.assignment2.game.Game;
import com.bd.assignment2.heart.Heart;
import com.bd.assignment2.project.Project;
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
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<Game> games;

    @OneToMany(mappedBy = "user")
    private List<Heart> hearts;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
