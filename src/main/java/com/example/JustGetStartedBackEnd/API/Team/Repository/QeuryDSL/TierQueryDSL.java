package com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import org.springframework.data.repository.query.Param;

public interface TierQueryDSL {
    Tier findByTierName(@Param("tierName") String tierName);
}
