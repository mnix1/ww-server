package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.play.*;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.war.RivalWarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;


@Service
@AllArgsConstructor
public class RivalRunService {
    private final RivalGlobalService rivalGlobalService;
    private final RivalWarService rivalWarService;
    private final RivalCampaignWarService rivalCampaignWarService;
    private final RivalBattleService rivalBattleService;
    private final RivalChallengeService rivalChallengeService;
    private final RivalProfileSeasonService rivalProfileSeasonService;

    public void run(RivalInit initContainer) {
        addProfileSeasons(initContainer);
        PlayManager manager = createManager(initContainer);
        initContainer.getProfiles().forEach(profile -> {
            if (!profile.getId().equals(BOT_PROFILE_ID)) {
                rivalGlobalService.put(profile.getId(), manager);
            }
        });
        manager.start();
    }

    public void addProfileSeasons(RivalInit initContainer) {
        if (initContainer.getImportance() != RivalImportance.RANKING || !(initContainer instanceof RivalTwoInit)) {
            return;
        }
        rivalProfileSeasonService.addProfileSeasons((RivalTwoInit) initContainer);
    }

    private PlayManager createManager(RivalInit initContainer) {
        RivalType type = initContainer.getType();
        if (type == RivalType.WAR) {
            return new PlayWarManager((RivalTwoInit) initContainer, rivalWarService);
        }
        if (type == RivalType.CAMPAIGN_WAR) {
            return new PlayCampaignManager((RivalCampaignWarInit) initContainer, rivalCampaignWarService);
        }
        if (type == RivalType.BATTLE) {
            return new PlayBattleManager((RivalTwoInit) initContainer, rivalBattleService);
        }
        if (type == RivalType.CHALLENGE) {
            return new PlayChallengeManager((RivalChallengeInit) initContainer, rivalChallengeService);
        }
        throw new IllegalArgumentException();
    }
}
