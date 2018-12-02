package com.ww.game.play;

import com.ww.game.training.TrainingManager;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTrainingInterval;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.service.rival.battle.RivalBattleService;

import java.util.Map;

import static com.ww.helper.TeamHelper.isBotProfile;
import static com.ww.websocket.message.MessageDTO.rivalContentMessage;

public class PlayBattleTrainingManager extends PlayBattleManager {
    private TrainingManager manager;

    public PlayBattleTrainingManager(RivalTwoInit init, RivalBattleService rivalService) {
        super(init, rivalService);
        this.manager = new TrainingManager(init.getOpponentProfile(), this);
    }

    @Override
    protected RivalInterval prepareInterval() {
        return new RivalTrainingInterval();
    }

    @Override
    public void send(Long profileId, Map<String, Object> model) {
        if (isBotProfile(profileId)) {
            manager.getConnection().sendMessage(rivalContentMessage(model));
        } else {
            super.send(profileId, model);
        }
    }
}
