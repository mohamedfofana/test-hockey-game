package com.maplr.testhockeygame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.maplr.testhockeygame.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	public Team findByYear(Long year);
}
