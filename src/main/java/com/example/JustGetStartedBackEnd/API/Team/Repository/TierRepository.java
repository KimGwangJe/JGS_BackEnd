package com.example.JustGetStartedBackEnd.API.Team.Repository;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL.TierQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierRepository extends JpaRepository<Tier,Long>, TierQueryDSL {
}
