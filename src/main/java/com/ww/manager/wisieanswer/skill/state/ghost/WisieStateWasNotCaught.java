package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotCaught extends WisieSkillState {
    private Long interval;

    public WisieStateWasNotCaught(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(MemberWisieStatus.NO_DISQUALIFICATION);
        manager.getWisieMember().removeDisguise();
        manager.getTeam(manager).getTeamSkills().unblockAll();
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (randomDouble(3 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConcentrationF1(),
                6 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConcentrationF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
