package com.ww.model.container.rival.battle;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalProfileContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BattleModelFactory extends RivalModelFactory {

    protected BattleContainer container;

    public BattleModelFactory(BattleContainer container) {
        this.container = container;
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("taskCount", BattleManager.TASK_COUNT);
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) container.getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("newScore", battleProfileContainer.getScore());
        model.put("newOpponentScore", ((BattleProfileContainer) container.getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }


    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelChoosingTaskProps(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) container.getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelClosed(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) container.getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }
}
