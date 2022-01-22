package com.maplr.testhockeygame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Team {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String coach;
	private Long year;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "team_id")
	private List<Player> players = new ArrayList<Player>();
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCoach() {
		return coach;
	}
	
	public void setCoach(String coach) {
		this.coach = coach;
	}
	
	public Long getYear() {
		return year;
	}
	
	public void setYear(Long year) {
		this.year = year;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
}
