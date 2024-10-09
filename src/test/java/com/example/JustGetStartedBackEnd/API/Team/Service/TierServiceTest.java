package com.example.JustGetStartedBackEnd.API.Team.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Tier;
import com.example.JustGetStartedBackEnd.API.Team.ExceptionType.TierExceptionType;
import com.example.JustGetStartedBackEnd.API.Team.Repository.TierRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TierServiceTest {

    @Mock
    private TierRepository tierRepository;

    @InjectMocks
    private TierService tierService;

    @Test
    @DisplayName("티어 엔티티 조회(이름) - 성공")
    void getTierByName_Success() {
        Tier tier = mock(Tier.class);
        when(tierRepository.findByTierName(anyString())).thenReturn(tier);

        tierService.getTierByName(anyString());

        verify(tierRepository, times(1)).findByTierName(anyString());
    }

    @Test
    @DisplayName("티어 엔티티 조회(이름) - 실패")
    void getTierByName_Fail() {
        when(tierRepository.findByTierName(anyString())).thenReturn(null);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> tierService.getTierByName(anyString()));

        assert(exception.getExceptionType()).equals(TierExceptionType.INVALID_TIER_NAME);
    }

    @Test
    @DisplayName("티어 엔티티 조회(아이디) - 성공")
    void getTierById_Success() {
        Tier tier = mock(Tier.class);
        when(tierRepository.findById(anyLong())).thenReturn(Optional.of(tier));

        tierService.getTierById(anyLong());

        verify(tierRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("티어 엔티티 조회(아이디) - 실패")
    void getTierById_Fail() {
        when(tierRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> tierService.getTierById(anyLong()));

        assert(exception.getExceptionType()).equals(TierExceptionType.INVALID_TIER_ID);
    }
}