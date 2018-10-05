package com.ww.model.container.rival.campaign;

import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.campaign.state.CampaignWarStateChosenWhoAnswer;
import com.ww.manager.rival.war.WarFlow;
import lombok.Getter;

import java.util.Map;

@Getter
public class CampaignWarFlow extends WarFlow {
    private CampaignWarManager manager;

    public CampaignWarFlow(CampaignWarManager manager) {
        super(manager);
        this.manager = manager;
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new CampaignWarStateChosenWhoAnswer(manager, profileId, content).startBoolean()) {
            dispose();
            phase1();
        }
    }
}
