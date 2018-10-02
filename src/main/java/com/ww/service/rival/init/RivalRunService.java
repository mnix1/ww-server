package com.ww.service.rival.init;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalCampaignWarInitContainer;
import com.ww.model.container.rival.init.RivalChallengeInitContainer;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.manager.rival.campaign.CampaignWarManager.BOT_PROFILE_ID;

@Service
public class RivalRunService {
    @Autowired
    private RivalGlobalService rivalGlobalService;
    @Autowired
    private RivalWarService rivalWarService;
    @Autowired
    private RivalCampaignWarService rivalCampaignWarService;
    @Autowired
    private RivalBattleService rivalBattleService;
    @Autowired
    private RivalChallengeService rivalChallengeService;
    @Autowired
    private ProfileConnectionService profileConnectionService;

    public void run(RivalInitContainer initContainer) {
        RivalManager rivalManager = createManager(initContainer);
        initContainer.getProfiles().forEach(profile -> {
            if (!profile.getId().equals(BOT_PROFILE_ID)) {
                rivalGlobalService.put(profile.getId(), rivalManager);
            }
        });
        rivalManager.getFlow().start();
    }

    RivalManager createManager(RivalInitContainer initContainer) {
        RivalType type = initContainer.getType();
        if (type == RivalType.WAR) {
            return new WarManager((RivalTwoPlayerInitContainer) initContainer, rivalWarService, profileConnectionService);
        }
        if (type == RivalType.CAMPAIGN_WAR) {
            return new CampaignWarManager((RivalCampaignWarInitContainer) initContainer, rivalCampaignWarService, profileConnectionService);
        }
        if (type == RivalType.BATTLE) {
            return new BattleManager((RivalTwoPlayerInitContainer) initContainer, rivalBattleService, profileConnectionService);
        }
        if (type == RivalType.CHALLENGE) {
            return new ChallengeManager((RivalChallengeInitContainer) initContainer, rivalChallengeService, profileConnectionService);
        }
        throw new IllegalArgumentException();
    }
}
