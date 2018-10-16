package com.ww.model.container.rival.campaign;

import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.campaign.state.CampaignWarStateChoosingWhoAnswer;
import com.ww.manager.rival.war.WarFlow;
import lombok.Getter;

@Getter
public class CampaignWarFlow extends WarFlow {
    private CampaignWarManager manager;

    public CampaignWarFlow(CampaignWarManager manager) {
        super(manager);
        this.manager = manager;
    }

    public synchronized void phase3() {
        addState(new CampaignWarStateChoosingWhoAnswer(manager)).addOnFlowableEndListener(aLong2 -> {
            phase1();
        }).startFlowable();
    }
}
