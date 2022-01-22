package com.maplr.testhockeygame.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;
import com.maplr.testhockeygame.service.TeamService;

@RestController
@RequestMapping(path = "/api/team", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {
	@Autowired
	TeamService teamService;
	
	@GetMapping("/{year}")
	public ResponseEntity<Team> findByYear(@PathVariable("year") Long year){
		final Team dbTeam =  teamService.findByYear(year);
		if (dbTeam == null) {
			return new ResponseEntity<Team>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Team>(dbTeam, HttpStatus.OK);
	}
	
	@PostMapping("/{year}")
	public ResponseEntity<Player> addPlayerAtYear(@PathVariable("year") Long year,@RequestBody Player player){
		final Player dbPlayer = teamService.addPlayerAtYear(year, player);
		if (dbPlayer == null) {
			return new ResponseEntity<Player>(HttpStatus.NOT_MODIFIED);
		}
		return new ResponseEntity<Player>(dbPlayer, HttpStatus.CREATED);
	}
}
