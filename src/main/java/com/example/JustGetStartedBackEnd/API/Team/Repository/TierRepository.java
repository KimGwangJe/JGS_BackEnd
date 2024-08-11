package com.example.JustGetStartedBackEnd.API.Team.Repository;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TierRepository extends JpaRepository<Tier,Long> {

    @Query("SELECT t FROM Tier t WHERE t.tierName = :tierName")
    Tier findByTierName(@Param("tierName") String tierName);
}
