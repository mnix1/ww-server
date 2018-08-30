package com.ww.model.container.rival.battle;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BattleContainer extends RivalContainer {

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("taskCount", BattleManager.TASK_COUNT);
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }

    public String findChoosingTaskPropsTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer p1Score =  ((BattleProfileContainer)rivalProfileContainers.get(0)).getScore();
        Integer p2Score =  ((BattleProfileContainer)rivalProfileContainers.get(1)).getScore();
        if (p1Score.equals(p2Score)) {
            return null;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return rivalProfileContainers.get(0).getProfile().getTag();
        }
        return rivalProfileContainers.get(1).getProfile().getTag();
    }

    public String findWinnerTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer p1Score = ((BattleProfileContainer)rivalProfileContainers.get(0)).getScore();
        Integer p2Score = ((BattleProfileContainer) rivalProfileContainers.get(1)).getScore();
        if (p1Score.equals(p2Score)) {
            return RivalManager.DRAW_WINNER_TAG;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return rivalProfileContainers.get(1).getProfile().getTag();
        }
        return rivalProfileContainers.get(0).getProfile().getTag();
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("newScore", battleProfileContainer.getScore());
        model.put("newOpponentScore", ((BattleProfileContainer) getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }


    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelChoosingTaskProps(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelClosed(model, rivalProfileContainer);
        BattleProfileContainer battleProfileContainer = (BattleProfileContainer) rivalProfileContainer;
        model.put("score", battleProfileContainer.getScore());
        model.put("opponentScore", ((BattleProfileContainer) getRivalProfileContainer(rivalProfileContainer.getOpponentId())).getScore());
    }
}
