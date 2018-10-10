package com.ww.service.rival.battle;

import com.ww.model.container.Resources;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import org.springframework.stereotype.Service;

@Service
public class RivalBattleService extends RivalService {
    @Override
    public void addRewardFromWin(Profile profile) {
        getRewardService().addSendRewardFromRivalWin(profile, new Resources(1L));
    }
}
