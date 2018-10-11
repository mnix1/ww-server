package com.ww.service.rival.init;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.war.RivalWarService;
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
    private RivalProfileSeasonService rivalProfileSeasonService;

    public void run(RivalInit initContainer) {
        addProfileSeasons(initContainer);
        RivalManager manager = createManager(initContainer);
        initContainer.getProfiles().forEach(profile -> {
            if (!profile.getId().equals(BOT_PROFILE_ID)) {
                rivalGlobalService.put(profile.getId(), manager);
            }
        });
        manager.getFlow().start();
    }

    public void addProfileSeasons(RivalInit initContainer) {
        if (initContainer.getImportance() != RivalImportance.RANKING || !(initContainer instanceof RivalTwoPlayerInit)) {
            return;
        }
        rivalProfileSeasonService.addProfileSeasons((RivalTwoPlayerInit) initContainer);
    }

    private RivalManager createManager(RivalInit initContainer) {
        RivalType type = initContainer.getType();
        if (type == RivalType.WAR) {
            return new WarManager((RivalTwoPlayerInit) initContainer, rivalWarService);
        }
        if (type == RivalType.CAMPAIGN_WAR) {
            return new CampaignWarManager((RivalCampaignWarInit) initContainer, rivalCampaignWarService);
        }
        if (type == RivalType.BATTLE) {
            return new BattleManager((RivalTwoPlayerInit) initContainer, rivalBattleService);
        }
        if (type == RivalType.CHALLENGE) {
            return new ChallengeManager((RivalChallengeInit) initContainer, rivalChallengeService);
        }
        throw new IllegalArgumentException();
    }
}
