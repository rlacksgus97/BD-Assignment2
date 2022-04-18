package com.bd.assignment2.game;

import com.bd.assignment2.game.dto.PublishGameReqDto;
import com.bd.assignment2.game.dto.ReadGameResDto;
import com.bd.assignment2.game.dto.SimpleGameResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Long> delete(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.delete(gameId));
    }

    @PostMapping("/like/{gameId}")
    public ResponseEntity<Long> like(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.like(gameId));
    }

    @GetMapping("/title")
    public ResponseEntity<List<SimpleGameResDto>> searchByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(gameService.searchByTitle(keyword));
    }

    @GetMapping("/user")
    public ResponseEntity<List<SimpleGameResDto>> searchByUser(@RequestParam String keyword) {
        return ResponseEntity.ok(gameService.searchByUser(keyword));
    }
}
