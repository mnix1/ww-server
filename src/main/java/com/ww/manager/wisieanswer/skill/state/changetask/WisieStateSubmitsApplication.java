package com.ww.manager.wisieanswer.skill.state.changetask;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateSubmitsApplication extends WisieSkillState {
    private Long interval;

    public WisieStateSubmitsApplication(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.SUBMITS_APPLICATION);
        interval = (long) (randomDouble(3 - manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getCunningF1(),
                4 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getCunningF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
