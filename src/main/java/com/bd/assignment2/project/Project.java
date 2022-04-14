package com.bd.assignment2.project;

import com.bd.assignment2.game.Game;
import com.bd.assignment2.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String code;

    @ManyToOne
    private User user;

    @OneToOne
    private Game game;
}
