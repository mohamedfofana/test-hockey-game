package com.maplr.testhockeygame.service;

import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;

public interface TeamService {

	public Team findByYear(Long year);
	
	public Player addPlayerAtYear(Long year, Player player);
	
}
