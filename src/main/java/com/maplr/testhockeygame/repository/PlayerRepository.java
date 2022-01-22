package com.maplr.testhockeygame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maplr.testhockeygame.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
}
