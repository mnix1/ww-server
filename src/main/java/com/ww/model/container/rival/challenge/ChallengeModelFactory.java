package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleModel;
import com.ww.model.container.rival.war.WarModel;
import com.ww.model.container.rival.war.WarModelFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ChallengeModelFactory extends WarModelFactory {

    public ChallengeModelFactory(WarModel model) {
        super(model);
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelBasic(model, profileContainer);
        ChallengeTeam challengeProfileContainer = (ChallengeTeam) profileContainer;
        model.put("score", challengeProfileContainer.getScore());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelAnswered(model, profileContainer);
        ChallengeTeam challengeProfileContainer = (ChallengeTeam) profileContainer;
        model.put("newScore", challengeProfileContainer.getScore());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalTeam profileContainer, boolean forceRandom) {
        super.fillModelChoosingTaskProps(model, profileContainer, forceRandom);
        ChallengeTeam challengeProfileContainer = (ChallengeTeam) profileContainer;
        model.put("score", challengeProfileContainer.getScore());
    }

    public void fillModelClosed(Map<String, Object> model, RivalTeam profileContainer) {
        super.fillModelClosed(model, profileContainer);
        ChallengeTeam challengeProfileContainer = (ChallengeTeam) profileContainer;
        model.put("score", challengeProfileContainer.getScore());
    }
}
