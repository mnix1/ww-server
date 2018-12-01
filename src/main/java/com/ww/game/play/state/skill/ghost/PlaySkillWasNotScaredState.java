package com.ww.game.play.state.skill.ghost;

import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;

import java.util.HashMap;
import java.util.Map;

public class PlaySkillWasNotScaredState extends PlaySkillOpponentState {

    public PlaySkillWasNotScaredState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    protected double minInterval() {
        return 4 - 2 * getOpponentWisie().getConfidenceF1() - 2 * getOpponentWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 6 - 3 * getOpponentWisie().getConfidenceF1() - 3 * getOpponentWisie().getConcentrationF1();
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void after() {
        Map<String, Object> params = new HashMap<>();
        params.put("profileId", opponentWarTeam.getProfileId());
        flow.run("DONE_GHOST", params);
    }
}
