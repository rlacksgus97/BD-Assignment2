package com.bd.assignment2.game;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.game.dto.PublishGameReqDto;
import com.bd.assignment2.game.dto.ReadGameResDto;
import com.bd.assignment2.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final JwtService jwtService;
    private final GameRepository gameRepository;

    public Long publish(Long projectId, PublishGameReqDto publishGameReqDto) {
        User user = jwtService.getUserFromJwt();
        Optional<Game> game = gameRepository.findByProjectId(projectId);
        if (game.isPresent()) {
            game.get().update(publishGameReqDto);
            return game.get().getId();
        } else {
            Game publishment = Game.builder()
                    .title(publishGameReqDto.getTitle())
                    .code(publishGameReqDto.getCode())
                    .build();
            gameRepository.save(publishment);
            user.addGame(publishment);
            return publishment.getId();
        }
    }

    public ReadGameResDto read(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게임입니다"));
        return ReadGameResDto.builder()
                .title(game.getTitle())
                .code(game.getCode())
                .userName(game.getUser().getEmail())
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
}
