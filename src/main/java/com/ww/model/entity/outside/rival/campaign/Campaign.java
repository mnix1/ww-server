package com.ww.model.entity.outside.rival.campaign;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private CampaignType type;
    private CampaignDestination destination;
    private Integer phases;
    private Long goldCost;
    private Long crystalCost;
    private Long wisdomCost;
    private Long elixirCost;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private DifficultyLevel difficultyLevel;

    public Campaign(CampaignType type, CampaignDestination destination, Integer phases, Long goldCost, Long crystalCost, Long wisdomCost, Long elixirCost, Long goldGain, Long crystalGain, Long wisdomGain, Long elixirGain) {
        this.type = type;
        this.destination = destination;
        this.phases = phases;
        this.goldCost = goldCost;
        this.crystalCost = crystalCost;
        this.wisdomCost = wisdomCost;
        this.elixirCost = elixirCost;
        this.goldGain = goldGain;
        this.crystalGain = crystalGain;
        this.wisdomGain = wisdomGain;
        this.elixirGain = elixirGain;
        if (destination == CampaignDestination.EASY) {
            this.difficultyLevel = DifficultyLevel.VERY_EASY;
        } else if (destination == CampaignDestination.NORMAL) {
            this.difficultyLevel = DifficultyLevel.NORMAL;
        } else if (destination == CampaignDestination.HARD) {
            this.difficultyLevel = DifficultyLevel.VERY_HARD;
        }
    }
}
