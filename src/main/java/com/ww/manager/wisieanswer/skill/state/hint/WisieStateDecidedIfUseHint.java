package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateDecidedIfUseHint extends WisieSkillState {
    private MemberWisieStatus action;
    private Long interval;

    public WisieStateDecidedIfUseHint(WisieAnswerManager manager, MemberWisieStatus action) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.action = action;
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval + ", action=" + action;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(action);
        interval = (long) (randomDouble(1 - manager.getWarWisie().getSpeedF1(), 2 - 2 * manager.getWarWisie().getSpeedF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
