package com.backend.tictactoe.GameController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.tictactoe.Exception.InvalidGameException;
import com.backend.tictactoe.Exception.InvalidParamException;
import com.backend.tictactoe.Exception.NotFoundException;
import com.backend.tictactoe.GameController.dto.ConnectRequest;
import com.backend.tictactoe.model.Game;
import com.backend.tictactoe.model.GamePlay;
import com.backend.tictactoe.model.Player;
import com.backend.tictactoe.service.GameService;
import com.backend.tictactoe.storage.GameStorage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    
    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connecttoGame(request.getPlayer(), request.getGameID()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameID(), game);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/view")
    public Map<String,Game> getgames(){
        return GameStorage.getInstance().getGames();
    }

    @GetMapping("/id/{gameID}")
    public int[][] getgames(@PathVariable("gameID") String gameID){
        return GameStorage.getInstance().getbyID(gameID).getBoard();
    }
    @GetMapping(path="/all")
    public Iterable<Game> getAllUsers() {
      // This returns a JSON or XML with the users
      return gameService.getgamerepo().findAll();
    }

}
