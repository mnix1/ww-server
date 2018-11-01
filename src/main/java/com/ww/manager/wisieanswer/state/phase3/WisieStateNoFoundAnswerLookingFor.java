package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateNoFoundAnswerLookingFor extends WisieState {
    private Long interval;

    public WisieStateNoFoundAnswerLookingFor(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(MemberWisieStatus.NO_FOUND_ANSWER_LOOKING_FOR);
        interval = (long) (randomDouble(2 - manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConfidenceF1(), 4 - 2 * manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConfidenceF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
