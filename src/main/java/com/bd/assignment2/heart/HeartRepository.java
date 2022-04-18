package com.bd.assignment2.heart;

import com.bd.assignment2.game.Game;
import com.bd.assignment2.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserAndGame(User user, Game game);
}
