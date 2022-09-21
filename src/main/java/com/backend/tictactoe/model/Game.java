package com.backend.tictactoe.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game extends Board{

    @Id 
    private String gameID;

    @Column(name = "Player_1")
    private String player1_name;

    @Column(name = "Player_2")
    private String player2_name;
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    @Enumerated(EnumType.STRING)
    private TicToe winner;
    
}
