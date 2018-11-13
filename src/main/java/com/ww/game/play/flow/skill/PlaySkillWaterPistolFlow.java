package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.waterpistol.PlaySkillCleanedState;
import com.ww.game.play.state.skill.waterpistol.PlaySkillCleaningState;
import com.ww.game.play.state.skill.waterpistol.PlaySkillWaterPistolUsedState;

public class PlaySkillWaterPistolFlow extends PlaySkillFlow {

    public PlaySkillWaterPistolFlow(MemberWisieManager opponentManager) {
        super(opponentManager);
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("WATER_PISTOL_USED", new PlaySkillWaterPistolUsedState(this, manager));
        stateMap.put("CLEANING", new PlaySkillCleaningState(this, manager));
        stateMap.put("CLEANED", new PlaySkillCleanedState(this, manager));
    }

    public void start() {
        run("WATER_PISTOL_USED");
    }
}
