package com.backend.tictactoe.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.tictactoe.Exception.InvalidGameException;
import com.backend.tictactoe.Exception.InvalidParamException;
import com.backend.tictactoe.Exception.NotFoundException;
import com.backend.tictactoe.dao.GameRepo;
import com.backend.tictactoe.model.Game;
import com.backend.tictactoe.model.GamePlay;
import com.backend.tictactoe.model.GameStatus;
import com.backend.tictactoe.model.Player;
import com.backend.tictactoe.model.TicToe;
import com.backend.tictactoe.storage.GameStorage;


@Service
public class GameService {
    
    @Autowired
    private final GameRepo gamerepo;

    public GameService(GameRepo gamerepo) {
        this.gamerepo = gamerepo;
    }

    public GameRepo getgamerepo(){
        return gamerepo;
    }
    
    public Game createGame(Player player){
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameID(String.valueOf(new Random().nextInt(4000)));
        game.setPlayer1_name(player.getPlayer());
        game.setStatus(GameStatus.NEW);
        gamerepo.save(game);
        GameStorage.getInstance().setGame(game);
        return game;

    }

    public Game connecttoGame(Player player2, String gameID) throws InvalidParamException,InvalidGameException{

        if (!GameStorage.getInstance().getGames().containsKey(gameID)) {
            throw new InvalidParamException("Game with provided id doesn't exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameID);

        if (game.getPlayer2_name() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }
        game.setPlayer2_name(player2.getPlayer());
        game.setStatus(GameStatus.INPROGRESS);
        gamerepo.save(game);
        GameStorage.getInstance().setGame(game);
        return game;

    }

    public Game connectToRandomGame(Player player2) throws NotFoundException{
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));
        
        game.setPlayer2_name(player2.getPlayer());
        game.setStatus(GameStatus.INPROGRESS);
        gamerepo.save(game);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameID())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameID());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }

        int[][] board = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
        Boolean oWinner = checkWinner(game.getBoard(), TicToe.O);

        if (xWinner) {
            game.setWinner(TicToe.X);
        } else if (oWinner) {
            game.setWinner(TicToe.O);
        }
        gamerepo.save(game);
        GameStorage.getInstance().setGame(game);
        return game;
    }
    private Boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArray = new int[9];
        int counterIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex] = board[i][j];
                counterIndex++;
            }
        }

        int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArray[winCombinations[i][j]] == ticToe.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Optional<Game> getGame(String id) throws InvalidParamException{
        if (gamerepo.findById(id) == null) {
            throw new InvalidParamException("Game with provided id doesn't exist");
        }else{
            Optional<Game> game = gamerepo.findById(id);
            return game;
        }
    }
}
    

