package com.ww.model.container.rival;

import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RivalResult {
    protected Boolean draw;
    protected Profile winner;
    protected Boolean resigned;

    public void winner(Profile winner) {
        this.draw = false;
        this.winner = winner;
    }

    public void draw() {
        this.draw = true;
    }

    public void winnerOpponentResigned(Profile winner) {
        winner(winner);
        resigned = true;
    }
}
