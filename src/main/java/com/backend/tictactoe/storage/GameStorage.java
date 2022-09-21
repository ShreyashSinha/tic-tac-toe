package com.backend.tictactoe.storage;

import java.util.HashMap;
import java.util.Map;

import com.backend.tictactoe.model.Game;

public class GameStorage {
    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameID(), game);
    }

    public Game getbyID(String gameID){
        return games.get(gameID);
    }
}
        
    

