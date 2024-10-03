package com.example.JustGetStartedBackEnd.API.Team.Entity;

import com.example.JustGetStartedBackEnd.API.Team.DTO.TierDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "tier")
@NoArgsConstructor
public class Tier {
    @Id
    @Column(name = "tier_id")
    private Long tierId;

    @NotBlank
    @Column(name = "tier_name")
    private String tierName;

    @OneToMany(mappedBy = "tier", fetch = FetchType.LAZY)
    private List<Team> team;

    public TierDTO tierDTO(){
        TierDTO tierDTO = new TierDTO();
        tierDTO.setTierId(this.getTierId());
        tierDTO.setTierName(this.tierName);
        return tierDTO;
    }

    @Builder
    public Tier(String tierName, Long tierId) {
        this.tierId = tierId;
        this.tierName = tierName;
    }
}
