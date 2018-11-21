package com.ww.game.play.action.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayAnswerAction;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

public class PlayWarAnswerAction extends PlayAnswerAction {

    public PlayWarAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    protected boolean correct(Long profileId, Map<String, Object> content) {
        return super.correct(profileId, content)
                && !((WarTeam) manager.getContainer().getTeams().team(profileId)).getActiveTeamMember().isWisie();
    }
}
