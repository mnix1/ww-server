package com.ww.game.play.modelfiller;

import com.ww.model.container.rival.challenge.ChallengeTeam;

import java.util.Map;

public class PlayChallengeModelFiller {
    public static void fillModelScore(Map<String, Object> model, ChallengeTeam challengeTeam) {
        model.put("score", challengeTeam.getScore());
    }

    public static void fillModelNewScore(Map<String, Object> model, ChallengeTeam challengeTeam) {
        model.put("newScore", challengeTeam.getScore());
    }

}
