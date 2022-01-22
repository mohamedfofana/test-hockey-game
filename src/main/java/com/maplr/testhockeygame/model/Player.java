package com.maplr.testhockeygame.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Player {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id; 
	private Long number;
    private String name;
    private String lastname;
    private String position;
    private Boolean isCaptain;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Team team;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
    @JsonProperty("isCaptain")
    @JsonInclude(Include.NON_NULL)
	public Boolean isCaptain() {
		return isCaptain;
	}
	public void setCaptain(Boolean isCaptain) {
		this.isCaptain = isCaptain;
	}
	
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Player other =  (Player) obj;
		if (!Objects.equals(number, other.number)) {
			return false;
		}else if (!Objects.equals(name, other.name)) {
			return false;
		}else if (!Objects.equals(lastname, other.lastname)) {
			return false;
		}else if (!Objects.equals(position, other.position)) {
			return false;
		}
		return  true;
	}
    
    
}
