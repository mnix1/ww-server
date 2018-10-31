package com.ww.play.modelfiller;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.model.container.rival.battle.BattleTeam;

import java.util.Map;

public class PlayBattleModelFiller {
    public static void fillModelTaskCount(Map<String, Object> model) {
        model.put("taskCount", BattleManager.TASK_COUNT);
    }

    public static void fillModelScores(Map<String, Object> model, BattleTeam battleTeam, BattleTeam battleOpponentTeam) {
        model.put("score", battleTeam.getScore());
        model.put("opponentScore", battleOpponentTeam.getScore());
    }

    public static void fillModelNewScores(Map<String, Object> model, BattleTeam battleTeam, BattleTeam battleOpponentTeam) {
        model.put("newScore", battleTeam.getScore());
        model.put("newOpponentScore", battleOpponentTeam.getScore());
    }

}
