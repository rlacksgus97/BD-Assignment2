package com.bd.assignment2.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByProjectId(Long projectId);
    List<Game> findByTitleContaining(String keyword);
    List<Game> findByUserContaining(String keyword);
}
