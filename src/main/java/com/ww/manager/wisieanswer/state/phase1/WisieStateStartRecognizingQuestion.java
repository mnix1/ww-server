package com.ww.manager.wisieanswer.state.phase1;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateStartRecognizingQuestion extends WisieState {
    private Long interval;

    public WisieStateStartRecognizingQuestion(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(MemberWisieStatus.WAITING_FOR_QUESTION);
        interval = (long) (randomDouble(1 - manager.getWarWisie().getReflexF1(), 3 - 2 * manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConcentrationF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
