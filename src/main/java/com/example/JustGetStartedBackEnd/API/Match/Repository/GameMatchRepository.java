package com.example.JustGetStartedBackEnd.API.Match.Repository;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Match.Repository.QueryDSL.GameMatchQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameMatchRepository extends JpaRepository<GameMatch, Long>, GameMatchQueryDSL {
}
