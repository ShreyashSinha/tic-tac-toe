package com.backend.tictactoe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.tictactoe.model.Game;

@Repository
public interface GameRepo extends JpaRepository<Game,String>{
    
}
