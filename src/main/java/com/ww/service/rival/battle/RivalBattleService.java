package com.ww.service.rival.battle;

import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import org.springframework.stereotype.Service;

@Service
public class RivalBattleService extends RivalService {
    @Override
    public void addRewardFromWin(Profile winner) {
        getRewardService().addRewardFromBattleWin(winner);
    }
}
