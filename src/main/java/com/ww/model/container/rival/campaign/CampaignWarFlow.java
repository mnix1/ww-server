package com.ww.model.container.rival.campaign;

import com.ww.manager.rival.campaign.state.CampaignWarStateChoosingWhoAnswer;
import com.ww.manager.rival.war.WarFlow;
import com.ww.manager.rival.war.WarManager;
import lombok.Getter;

@Getter
public class CampaignWarFlow extends WarFlow {

    public CampaignWarFlow(WarManager manager) {
        super(manager);
    }

    public synchronized void phase3() {
        addState(new CampaignWarStateChoosingWhoAnswer(manager)).addOnFlowableEndListener(aLong2 -> {
            phase1();
        }).startFlowable();
    }
}
