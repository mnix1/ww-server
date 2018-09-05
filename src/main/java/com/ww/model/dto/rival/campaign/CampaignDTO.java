package com.ww.model.dto.rival.campaign;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.entity.rival.campaign.Campaign;
import lombok.Getter;

@Getter
public class CampaignDTO {
    private CampaignType type;
    private CampaignDestination destination;
    private Long goldCost;
    private Long crystalCost;
    private Long wisdomCost;
    private Long elixirCost;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private Integer rating;

    public CampaignDTO(Campaign campaign) {
        this.type = campaign.getType();
        this.destination = campaign.getDestination();
        this.goldCost = campaign.getGoldCost();
        this.crystalCost = campaign.getCrystalCost();
        this.wisdomCost = campaign.getWisdomCost();
        this.elixirCost = campaign.getElixirCost();
        this.goldGain = campaign.getGoldGain();
        this.crystalGain = campaign.getCrystalGain();
        this.wisdomGain = campaign.getWisdomGain();
        this.elixirGain = campaign.getElixirGain();
        this.rating = campaign.getDifficultyLevel().getRating();
    }

    public boolean isFree() {
        return goldCost + crystalCost + wisdomCost + elixirCost == 0;
    }
}
