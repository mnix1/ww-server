package com.ww.service.rival.init;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalInitContainer;
import org.springframework.stereotype.Service;

@Service
public class RivalRunService {
    public void run(RivalInitContainer rivalInitContainer) {
    }

    RivalManager createManager(RivalType type) {
        if (type == RivalType.WAR) {
            return new WarManager();
        }
        if (type == RivalType.CAMPAIGN_WAR) {
            return new CampaignWarManager();
        }
        if (type == RivalType.BATTLE) {
            return new BattleManager();
        }
        if (type == RivalType.CHALLENGE) {
            return new ChallengeManager();
        }
    }
}
