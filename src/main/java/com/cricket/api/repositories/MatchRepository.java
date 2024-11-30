package com.cricket.api.repositories;

import com.cricket.api.entities.Match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match,Integer> {

    //fetching details by teamHeading
    Optional<Match> findByTeamHeading(String teamHeading);
}
