package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateRemovingDisguise extends WisieSkillState {
    private Long interval;

    public WisieStateRemovingDisguise(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }


    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(MemberWisieStatus.REMOVING_DISGUISE);
        interval = (long) (randomDouble(3 - manager.getWarWisie().getSpeedF1(),
                6 - 4 * manager.getWarWisie().getSpeedF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
