package com.maplr.testhockeygame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.service.PlayerService;

@RestController
@RequestMapping(path = "/api/player", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {
	@Autowired
	PlayerService playerService;
	
    @PutMapping("/captain/{id}")
    public ResponseEntity<Player> setCaptain(@PathVariable("id") Long id) {
        final Player dbPlayer = this.playerService.setCaptain(id);
        if (dbPlayer == null ) {
        	return new ResponseEntity<Player>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<Player>(dbPlayer, HttpStatus.OK);
    }
}
