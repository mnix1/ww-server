package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BattleTeam extends RivalTeam {
    private Integer score = 0;

    public BattleTeam(Profile profile) {
        super(profile);
    }

    public void updateScore(boolean isCorrect, int points) {
        if (isCorrect) {
            score += points;
        } else {
            score -= points;
        }
    }
}
