package com.ww.model.container.rival;

import java.util.Map;

public interface RivalTeamSkills {
    Map<String, Integer> prepareSkills();

    boolean canUseHint();

    void useHint();

    boolean canUseWaterPistol();

    void useWaterPistol();

    boolean canUseLifebuoy();

    void useLifebuoy();
}
