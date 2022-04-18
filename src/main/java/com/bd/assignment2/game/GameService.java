package com.bd.assignment2.game;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.game.dto.PublishGameReqDto;
import com.bd.assignment2.game.dto.ReadGameResDto;
import com.bd.assignment2.game.dto.SimpleGameResDto;
import com.bd.assignment2.heart.Heart;
import com.bd.assignment2.heart.HeartRepository;
import com.bd.assignment2.project.Project;
import com.bd.assignment2.project.ProjectRepository;
import com.bd.assignment2.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final JwtService jwtService;
    private final ProjectRepository projectRepository;
    private final GameRepository gameRepository;
    private final HeartRepository heartRepository;

    public Long publish(Long projectId, PublishGameReqDto publishGameReqDto) {
        User user = jwtService.getUserFromJwt();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        Optional<Game> game = gameRepository.findByProjectId(projectId);
        if (game.isPresent()) {
            game.get().update(publishGameReqDto);
            return game.get().getId();
        } else {
            Game publishment = Game.builder()
                    .title(publishGameReqDto.getTitle())
                    .code(publishGameReqDto.getCode())
                    .build();
            publishment.setProject(project);
            gameRepository.save(publishment);

            user.addGame(publishment);

            return publishment.getId();
        }
    }

    public ReadGameResDto read(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게임입니다"));
        game.watched();
        return ReadGameResDto.builder()
                .title(game.getTitle())
                .code(game.getCode())
                .userName(game.getUser().getEmail())
                .hit(game.getHit())
                .build();
    }

    public Long delete(Long gameId) {
        User user = jwtService.getUserFromJwt();
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게임입니다"));
        if (user.equals(game.getUser())) {
            gameRepository.delete(game);
        } else {
            throw new RuntimeException("게임을 삭제할 권한이 없습니다");
        }
        return gameId;
    }

    public Long like(Long gameId) {
        User user = jwtService.getUserFromJwt();
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게임입니다"));
        Optional<Heart> heart = heartRepository.findByUserAndGame(user, game);
        if (heart.isEmpty()) {
            Heart newHeart = Heart.builder()
                    .build();
            user.addHeart(heart.get());
            game.addHeart(heart.get());
            return heartRepository.save(newHeart).getId();
        } else {
            heartRepository.delete(heart.get());
            return heart.get().getId();
        }
    }

    public List<SimpleGameResDto> searchByTitle(String keyword) {
        List<Game> games = gameRepository.findByTitleContaining(keyword);
        List<SimpleGameResDto> simpleGameResDtos = new ArrayList<>();
        if (games.size() != 0) {
            for (Game g : games) {
                simpleGameResDtos.add(SimpleGameResDto.toDto(g));
            }
        }
        return simpleGameResDtos;
    }

    public List<SimpleGameResDto> searchByUser(String keyword) {
        List<Game> games = gameRepository.findByUserContaining(keyword);
        List<SimpleGameResDto> simpleGameResDtos = new ArrayList<>();
        if (games.size() != 0) {
            for (Game g : games) {
                simpleGameResDtos.add(SimpleGameResDto.toDto(g));
            }
        }
        return simpleGameResDtos;
    }
}
