package com.maplr.testhockeygame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.maplr.testhockeygame.exception.CustomResponseException;
import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;
import com.maplr.testhockeygame.repository.PlayerRepository;
import com.maplr.testhockeygame.repository.TeamRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

	@Autowired
	TeamRepository teamRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSetCaptain3PlayerTeam_OK() throws Exception {
		// Given 
		Team team = new Team();
		team.setCoach("Dominique Ducharme");
		team.setYear(2021L);

		Player player = new Player();
		player.setNumber(99L);
		player.setName("Marion");
		player.setLastname("Félix");
		player.setPosition("forward");
		player.setCaptain(false);
		team.getPlayers().add(player);

		player = new Player();
		player.setNumber(10L);
		player.setName("Alice");
		player.setLastname("Never");
		player.setPosition("attacking midfield");
		player.setCaptain(false);
		team.getPlayers().add(player);

		player = new Player();
		player.setNumber(5L);
		player.setName("Carey");
		player.setLastname("Price");
		player.setPosition("defensive midfield");
		player.setCaptain(true);
		team.getPlayers().add(player);

		teamRepository.save(team);

		// When 
		final Optional<Player> dbPlayer = playerRepository.findAll()
				.stream()
				.filter(p -> p.getNumber().equals(99L))
				.findFirst();
		final ResultActions resultActions = mockMvc.perform(put("/api/player/captain/"+ dbPlayer.get().getId()));

		// Then 
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("number", is(99)))
		.andExpect(jsonPath("name", is("Marion")))
		.andExpect(jsonPath("lastname", is("Félix")))
		.andExpect(jsonPath("position", is("forward")))
		.andExpect(jsonPath("isCaptain", is(true)));
	}

	@Test
	public void testSetCaptain_AlReadyCaptain_OK() throws Exception {
		// Given 
		Team team = new Team();
		team.setCoach("Dominique Ducharme");
		team.setYear(2021L);

		final Player player99 = new Player();
		player99.setNumber(99L);
		player99.setName("Marion");
		player99.setLastname("Félix");
		player99.setPosition("forward");
		player99.setCaptain(true);
		team.getPlayers().add(player99);

		Player player = new Player();
		player.setNumber(5L);
		player.setName("Carey");
		player.setLastname("Price");
		player.setPosition("defensive midfield");
		player.setCaptain(false);
		team.getPlayers().add(player);

		teamRepository.save(team);

		// When 
		final Optional<Player> dbPlayer = playerRepository.findAll()
				.stream()
				.filter(p -> p.getNumber().equals(99L))
				.findFirst();
		final ResultActions resultActions = mockMvc.perform(put("/api/player/captain/"+ dbPlayer.get().getId()));

		// Then 
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("number", is(99)))
												.andExpect(jsonPath("name", is(player99.getName())))
												.andExpect(jsonPath("lastname", is(player99.getLastname())))
												.andExpect(jsonPath("position", is(player99.getPosition())))
												.andExpect(jsonPath("isCaptain", is(player99.isCaptain())));
	}

	@Test
	public void testSetCaptain_No_Player_Exception_KO() throws Exception {
		// Given 
		Team team = new Team();
		team.setCoach("Dominique Ducharme");
		team.setYear(2021L);

		Player player = new Player();
		player.setNumber(99L);
		player.setName("Marion");
		player.setLastname("Félix");
		player.setPosition("forward");
		player.setCaptain(false);
		team.getPlayers().add(player);

		final Team t = teamRepository.save(team);

		// When
		try {
			mockMvc.perform(put("/api/player/captain/"+ t.getPlayers().get(0).getId()+1));
			failBecauseExceptionWasNotThrown(CustomResponseException.class);
		}catch(Exception e) {
			// Then 
			assertThat(e instanceof CustomResponseException);
			assertEquals("No Player found with the id "+t.getPlayers().get(0).getId()+1, e.getCause().getMessage().toString());
		}

	}
	
	@Test
	public void testSetCaptain_No_Team_Exception_KO() throws Exception {
		// Given 

		Player player = new Player();
		player.setNumber(99L);
		player.setName("Marion");
		player.setLastname("Félix");
		player.setPosition("forward");
		player.setCaptain(false);

		final Player t = playerRepository.save(player);

		// When
		try {
			mockMvc.perform(put("/api/player/captain/"+ t.getId()));
			failBecauseExceptionWasNotThrown(CustomResponseException.class);
		}catch(Exception e) {
			// Then 
			assertThat(e instanceof CustomResponseException);
			assertEquals("No team is associated with the player "+t.getId(), e.getCause().getMessage().toString());
		}

	}

}