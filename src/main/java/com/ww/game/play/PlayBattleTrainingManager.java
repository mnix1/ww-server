package com.ww.game.play;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTrainingInterval;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.service.rival.battle.RivalBattleService;

import java.util.Map;

import static com.ww.helper.TeamHelper.isBotProfile;

public class PlayBattleTrainingManager extends PlayBattleManager {
    public PlayBattleTrainingManager(RivalTwoInit init, RivalBattleService rivalService) {
        super(init, rivalService);
    }

    @Override
    protected RivalInterval prepareInterval() {
        return new RivalTrainingInterval();
    }

    public void send(Long profileId, Map<String, Object> model) {
        if (isBotProfile(profileId)) {

        } else {
            super.send(profileId, model);
        }
    }
}
