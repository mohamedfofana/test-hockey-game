package com.maplr.testhockeygame.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maplr.testhockeygame.exception.CustomResponseException;
import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;
import com.maplr.testhockeygame.repository.PlayerRepository;
import com.maplr.testhockeygame.repository.TeamRepository;
import com.maplr.testhockeygame.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	TeamRepository teamRepository;

	@Override
	public Player setCaptain(Long id) {
		// find the player
		final Optional<Player> dbPlayer = playerRepository.findById(id);
		if(dbPlayer.isPresent()) {
			final Player newCaptain = dbPlayer.get();
			if (!dbPlayer.get().isCaptain()) {
				// find the Team
				final Team playerTeam = newCaptain.getTeam();
				if (playerTeam== null) {
					throw new CustomResponseException("No team is associated with the player " + id);
				}
				// find the current capitain and remove the captaincy
				final Optional<Player> oldCaptain = playerTeam.getPlayers()
						.stream()
						.filter(p -> p.isCaptain())
						.findFirst(); 					
				if (oldCaptain.isPresent()) {
					oldCaptain.get().setCaptain(false);
					playerRepository.save(oldCaptain.get());
				}
				// Set the new Captain
				newCaptain.setCaptain(true);
				playerRepository.save(newCaptain);
			}			
			return newCaptain;
		}
		throw new CustomResponseException("No Player found with the id " + id);
	}
	
}
