package com.maplr.testhockeygame.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maplr.testhockeygame.exception.CustomResponseException;
import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;
import com.maplr.testhockeygame.repository.PlayerRepository;
import com.maplr.testhockeygame.repository.TeamRepository;
import com.maplr.testhockeygame.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {


	@Autowired
	TeamRepository teamRepository;

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public Team findByYear(Long year) {
		final Team dbTeam = teamRepository.findByYear(year);
		if (dbTeam != null) {
			// This will allow to show only for the captain the field isCaptain in the http response  
			final List<Player> newPlayers = dbTeam.getPlayers().stream()
					.map(p -> {
						if (!p.isCaptain()) {
							p.setCaptain(null);
						}
						return p;
					})
					.collect(Collectors.toList());	
			dbTeam.setPlayers(newPlayers);
			return dbTeam;
		}
		throw new CustomResponseException("No team was found with at year " + year);

	}

	@Override
	public Player addPlayerAtYear(Long year, Player player) {
		final Team dbTeam = teamRepository.findByYear(year);;
		if (dbTeam != null) {
			if (dbTeam.getPlayers().contains(player)) {
				return player;
			}else {
				player.setTeam(dbTeam);
				final Player dbPlayer = playerRepository.save(player);
				return dbPlayer; 
			}
		}
		throw new CustomResponseException("No team was found with at year " + year);
	}


};
;