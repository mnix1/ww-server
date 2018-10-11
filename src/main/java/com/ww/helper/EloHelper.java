package com.ww.helper;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;

public class EloHelper {

    public static final int WINNER = 1;
    public static final int LOOSER = -1;
    public static final int DRAW = 0;
    private static final int FACTOR_K = 32;
    public static final long MIN_ELO = 0;

    public static Long prepareEloChange(Long profileElo, Long opponentElo, int result) {
        double score;
        if (result == WINNER) {
            score = 1.0;
        } else if (result == LOOSER) {
            score = 0;
        } else {
            score = 0.5;
        }
        double eloChange = FACTOR_K * (score - (1 / (1 + (Math.pow(10, (opponentElo - profileElo) / 400d)))));
        return Math.round(eloChange);
    }
}
