package com.ww.game.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.PlayManager;
import com.ww.game.play.flow.PlayWarFlow;

import java.util.Map;

public class PlayChooseWhoAnswerAction extends PlayAction {

    public PlayChooseWhoAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if ((!flow.isStatusEquals(RivalStatus.CHOOSING_WHO_ANSWER)
                && !flow.isStatusEquals(RivalStatus.CHOSEN_WHO_ANSWER))
                || !content.containsKey("activeIndex")) {
            return;
        }
        try {
            Integer activeIndex = (Integer) content.get("activeIndex");
            WarTeam warTeam = (WarTeam) container.getTeams().team(profileId);
            if (warTeam.isChosenActiveIndex()
                    || !warTeam.getPresentIndexes().contains(activeIndex)) {
                return;
            }
            ((PlayWarFlow) flow).chosenWhoAnswerAction(profileId, activeIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
