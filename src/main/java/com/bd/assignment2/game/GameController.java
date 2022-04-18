package com.bd.assignment2.game;

import com.bd.assignment2.game.dto.PublishGameReqDto;
import com.bd.assignment2.game.dto.ReadGameResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/{projectId}")
    public ResponseEntity<Long> publish(@PathVariable Long projectId, @RequestBody PublishGameReqDto publishGameReqDto) {
        return ResponseEntity.ok(gameService.publish(projectId, publishGameReqDto));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ReadGameResDto> read(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.read(gameId));
    }

    @DeleteMapping("{/gameId}")
    public ResponseEntity<Long> delete(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.delete(gameId));
    }
}
