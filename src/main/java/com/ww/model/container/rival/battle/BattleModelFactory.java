package com.ww.model.container.rival.battle;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BattleModelFactory extends RivalModelFactory {

    protected BattleModel container;

    public BattleModelFactory(BattleModel container) {
        this.container = container;
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelBasic(model, profileContainer);
        BattleTeam battleProfileContainer = (BattleTeam) profileContainer;
        model.put("taskCount", BattleManager.TASK_COUNT);
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeam(profileContainer.getProfileId()).getScore());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelAnswered(model, profileContainer);
        BattleTeam battleProfileContainer = (BattleTeam) profileContainer;
        model.put("newScore", battleProfileContainer.getScore());
        model.put("newOpponentScore", container.getTeamsContainer().opponentTeam(profileContainer.getProfileId()).getScore());
    }


    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelChoosingTaskProps(model, profileContainer);
        BattleTeam battleProfileContainer = (BattleTeam) profileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeam(profileContainer.getProfileId()).getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelClosed(model, profileContainer);
        BattleTeam battleProfileContainer = (BattleTeam) profileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeam(profileContainer.getProfileId()).getScore());
    }
}
