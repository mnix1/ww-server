package com.ww.helper;

public class EloHelper {

    public static final int WINNER = 1;
    public static final int LOOSER = -1;
    public static final int DRAW = 0;
    private static final int FACTOR_K = 32;
    private static final long MIN_ELO = 0;

    public static Long prepareNewElo(Long profileElo, Long opponentElo, int result) {
        double score;
        if (result == WINNER) {
            score = 1.0;
        } else if (result == LOOSER) {
            score = 0;
        } else {
            score = 0.5;
        }
        double eloChange = FACTOR_K * (score - (1 / (1 + (Math.pow(10, (opponentElo - profileElo) / 400)))));
        return Math.max(MIN_ELO, Math.round(profileElo + eloChange));
    }
}
