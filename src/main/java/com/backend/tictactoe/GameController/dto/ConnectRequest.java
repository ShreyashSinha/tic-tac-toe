package com.backend.tictactoe.GameController.dto;


import com.backend.tictactoe.model.Player;

import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameID;
}
