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

    protected BattleModel model;

    public BattleModelFactory(BattleModel model) {
        this.model = model;
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeam team) {
        super.fillModelBasic(model, team);
        BattleTeam battleProfileContainer = (BattleTeam) team;
        model.put("taskCount", BattleManager.TASK_COUNT);
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleTeam)this.model.getTeams().opponent(team)).getScore());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeam team) {
        super.fillModelAnswered(model, team);
        BattleTeam battleProfileContainer = (BattleTeam) team;
        model.put("newScore", battleProfileContainer.getScore());
        model.put("newOpponentScore", ((BattleTeam)this.model.getTeams().opponent(team)).getScore());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalTeam team, boolean forceRandom) {
        super.fillModelChoosingTaskProps(model, team, forceRandom);
        BattleTeam battleProfileContainer = (BattleTeam) team;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleTeam)this.model.getTeams().opponent(team)).getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalTeam team) {
        super.fillModelClosed(model, team);
        BattleTeam battleProfileContainer = (BattleTeam) team;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore",((BattleTeam)this.model.getTeams().opponent(team)).getScore());
    }
}
