package com.maplr.testhockeygame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.maplr.testhockeygame.exception.CustomResponseException;
import com.maplr.testhockeygame.model.Player;
import com.maplr.testhockeygame.model.Team;
import com.maplr.testhockeygame.repository.PlayerRepository;
import com.maplr.testhockeygame.repository.TeamRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TeamControllerTest {
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testAddPlayerAtYear_OK() throws Exception {
		// Given 
		Team team = new Team();
		team.setCoach("Dominique Ducharme");
		team.setYear(2020L);

		Player player = new Player();
		player.setNumber(5L);
		player.setName("Carey");
		player.setLastname("Price");
		player.setPosition("defensive midfield");
		player.setCaptain(false);
		team.getPlayers().add(player);

		player = new Player();
		player.setNumber(10L);
		player.setName("Alice");
		player.setLastname("Never");
		player.setPosition("attacking midfield");
		player.setCaptain(true);
		team.getPlayers().add(player);

		teamRepository.save(team);
		
		// When 
		final ResultActions resultActions = mockMvc.perform(post("/api/team/"+ team.getYear())
												   .contentType(MediaType.APPLICATION_JSON)
												   .content("{\n"
												   		+ "  \"number\":99,\n"
												   		+ "  \"name\":\"Marion\",\n"
												   		+ "  \"lastname\":\"Félix\",\n"
												   		+ "  \"position\":\"forward\",\n"
												   		+ "  \"isCaptain\" : false\n"
												   		+ "}\n"));

		// Then 
		resultActions.andExpect(status().isCreated()).andExpect(jsonPath("number", is(99)))
													 .andExpect(jsonPath("name", is("Marion")))
													 .andExpect(jsonPath("lastname", is("Félix")))
													 .andExpect(jsonPath("position", is("forward")))
													 .andExpect(jsonPath("isCaptain", is(false)));
	}
	@Test
	public void testAddPlayerAtYear_ExistingPlayer_OK() throws Exception {
		// Given 
		Team team = new Team();
		team.setCoach("Dominique Ducharme");
		team.setYear(2021L);

		final Player player99 = new Player();
		player99.setNumber(99L);
		player99.setName("Marion");
		player99.setLastname("Félix");
		player99.setPosition("forward");
		player99.setCaptain(false);
		team.getPlayers().add(player99);

		Player player = new Player();
		player.setNumber(10L);
		player.setName("Alice");
		player.setLastname("Never");
		player.setPosition("attacking midfield");
		player.setCaptain(true);
		team.getPlayers().add(player);
		
		player = new Player();
		player.setNumber(5L);
		player.setName("Carey");
		player.setLastname("Price");
		player.setPosition("defensive midfield");
		player.setCaptain(false);
		team.getPlayers().add(player);
		
		teamRepository.save(team);
		
		// When 
		final ResultActions resultActions = mockMvc.perform(post("/api/team/"+ team.getYear())
												   .contentType(MediaType.APPLICATION_JSON)
												   .content("{\n"
												   		+ "  \"number\":99,\n"
												   		+ "  \"name\":\"Marion\",\n"
												   		+ "  \"lastname\":\"Félix\",\n"
												   		+ "  \"position\":\"forward\",\n"
												   		+ "  \"isCaptain\" : false\n"
												   		+ "}\n"));

		// Then 
		resultActions.andExpect(status().isCreated()).andExpect(jsonPath("number", is(99)))
													 .andExpect(jsonPath("name", is(player99.getName())))
													 .andExpect(jsonPath("lastname", is(player99.getLastname())))
													 .andExpect(jsonPath("position", is(player99.getPosition())))
													 .andExpect(jsonPath("isCaptain", is(player99.isCaptain())));
	}

	@Test
	public void testAddPlayerAtYear_NoTeam_Exception_KO() throws Exception {
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

				teamRepository.save(team);

				// When
				final Long year = 2019L;
				try {
					mockMvc.perform(post("/api/team/"+ year)
							   .contentType(MediaType.APPLICATION_JSON)
							   .content("{\n"
							   		+ "  \"number\":99,\n"
							   		+ "  \"name\":\"Marion\",\n"
							   		+ "  \"lastname\":\"Félix\",\n"
							   		+ "  \"position\":\"forward\",\n"
							   		+ "  \"isCaptain\" : false\n"
							   		+ "}\n"));
					failBecauseExceptionWasNotThrown(CustomResponseException.class);
				}catch(Exception e) {
					// Then 
					assertThat(e instanceof CustomResponseException);
					assertEquals("No team was found with at year " + year, e.getCause().getMessage().toString());
				}
	}

	@Test
	public void testFindByYear_OK() throws Exception {
		// Given 
				Team team = new Team();
				team.setCoach("Dominique Ducharme");
				team.setYear(2018L);

				final Player player99 = new Player();
				player99.setNumber(99L);
				player99.setName("Marion");
				player99.setLastname("Félix");
				player99.setPosition("forward");
				player99.setCaptain(false);
				team.getPlayers().add(player99);

				final Player player10 = new Player();
				player10.setNumber(10L);
				player10.setName("Alice");
				player10.setLastname("Never");
				player10.setPosition("attacking midfield");
				player10.setCaptain(false);
				team.getPlayers().add(player10);
				
				final Player player5 = new Player();
				player5.setNumber(5L);
				player5.setName("Carey");
				player5.setLastname("Price");
				player5.setPosition("defensive midfield");
				player5.setCaptain(true);
				team.getPlayers().add(player5);
				
				teamRepository.save(team);
				
				// When 
				final ResultActions resultActions = mockMvc.perform(get("/api/team/"+ team.getYear()));

				// Then 
				resultActions.andExpect(status().isOk())
							 .andExpect(jsonPath("coach", is("Dominique Ducharme")))
							 .andExpect(jsonPath("year", is(2018)))
							 
							 .andExpect(jsonPath("players[0].number", is(99)))
							 .andExpect(jsonPath("players[0].name", is(player99.getName())))
							 .andExpect(jsonPath("players[0].lastname", is(player99.getLastname())))
							 .andExpect(jsonPath("players[0].position", is(player99.getPosition())))
				
							 .andExpect(jsonPath("players[1].number", is(10)))
							 .andExpect(jsonPath("players[1].name", is(player10.getName())))
							 .andExpect(jsonPath("players[1].lastname", is(player10.getLastname())))
							 .andExpect(jsonPath("players[1].position", is(player10.getPosition())))
							
							 .andExpect(jsonPath("players[2].number", is(5)))
							 .andExpect(jsonPath("players[2].name", is(player5.getName())))
							 .andExpect(jsonPath("players[2].lastname", is(player5.getLastname())))
							 .andExpect(jsonPath("players[2].position", is(player5.getPosition())))
							 .andExpect(jsonPath("players[2].isCaptain", is(true)));
	}

	@Test
	public void testFindByYear_NoTeam_Exception_KO() throws Exception {
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

		teamRepository.save(team);
		// When
		final Long year = 2019L;
		try {
			mockMvc.perform(get("/api/team/"+ year));
			failBecauseExceptionWasNotThrown(CustomResponseException.class);
		}catch(Exception e) {
			// Then 
			assertThat(e instanceof CustomResponseException);
			assertEquals("No team was found with at year " + year, e.getCause().getMessage().toString());
		}
	}
}
