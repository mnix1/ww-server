package com.ww.game.play.modelfiller;

import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

public interface PlayModelPreparer {
    Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam);

}
