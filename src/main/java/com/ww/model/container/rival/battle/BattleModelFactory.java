package com.ww.model.container.rival.battle;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeamContainer;
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

    public void fillModelBasic(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelBasic(model, profileContainer);
        BattleTeamContainer battleProfileContainer = (BattleTeamContainer) profileContainer;
        model.put("taskCount", BattleManager.TASK_COUNT);
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getScore());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelAnswered(model, profileContainer);
        BattleTeamContainer battleProfileContainer = (BattleTeamContainer) profileContainer;
        model.put("newScore", battleProfileContainer.getScore());
        model.put("newOpponentScore", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getScore());
    }


    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelChoosingTaskProps(model, profileContainer);
        BattleTeamContainer battleProfileContainer = (BattleTeamContainer) profileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelClosed(model, profileContainer);
        BattleTeamContainer battleProfileContainer = (BattleTeamContainer) profileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getScore());
    }
}
