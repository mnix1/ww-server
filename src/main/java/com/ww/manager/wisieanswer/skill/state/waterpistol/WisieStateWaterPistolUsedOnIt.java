package com.ww.manager.wisieanswer.skill.state.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWaterPistolUsedOnIt extends WisieSkillState {
    private Long interval;

    public WisieStateWaterPistolUsedOnIt(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.WATER_PISTOL_USED_ON_IT);
        manager.getTeam(manager).getActiveTeamMember().addDisguise(DisguiseType.PENGUIN_RAIN);
        manager.getWarManager().sendActiveMemberAndActionsModel();
        interval = (long) (randomDouble(7 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConcentrationF1() - manager.getWarWisie().getConfidenceF1(),
                9 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConcentrationF1() - 2 * manager.getWarWisie().getConfidenceF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
