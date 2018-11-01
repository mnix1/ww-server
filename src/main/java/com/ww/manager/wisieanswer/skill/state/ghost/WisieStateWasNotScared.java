package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotScared extends WisieSkillState {
    private Long interval;

    public WisieStateWasNotScared(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        //actions in WisieStateScareSucceeded
//        warManager.addAction(MemberWisieStatus.WAS_NOT_SCARED);
        interval = (long) (randomDouble(3 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConcentrationF1(),
                4 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConcentrationF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
