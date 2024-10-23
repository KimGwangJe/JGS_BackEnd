package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TierExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TierRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierService {
    private final TierRepository tierRepository;

    @Transactional(readOnly = true)
    public Tier getTierByName(String tierName){
        Tier tier = tierRepository.findByTierName(tierName);
        if(tier == null){
            log.warn("Invalid tier name : {}", tierName);
            throw new BusinessLogicException(TierExceptionType.INVALID_TIER_NAME);
        }
        return tier;
    }

    @Transactional(readOnly = true)
    public Tier getTierById(Long tierId){
        return tierRepository.findById(tierId).orElseThrow(
                () -> new BusinessLogicException(TierExceptionType.INVALID_TIER_ID));
    }
}
