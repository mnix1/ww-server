package com.ww.model.container.rival;

import java.util.HashMap;
import java.util.Map;

public class EmptyTeamSkills implements RivalTeamSkills {

    @Override
    public boolean canUseHint() {
        return false;
    }

    @Override
    public void useHint() {
    }

    @Override
    public boolean canUseWaterPistol() {
        return false;
    }

    @Override
    public void useWaterPistol() {
    }

    @Override
    public boolean canUseLifebuoy() {
        return false;
    }

    @Override
    public void useLifebuoy() {
    }

    @Override
    public Map<String, Integer> prepareSkills() {
        return new HashMap<>();
    }
}
