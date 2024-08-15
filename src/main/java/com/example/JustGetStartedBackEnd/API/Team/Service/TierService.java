package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TierExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TierRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TierService {
    private final TierRepository tierRepository;

    @Transactional(readOnly = true)
    public Tier getTierByName(String tierName){
        Tier tier = tierRepository.findByTierName(tierName);
        if(tier == null){
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
