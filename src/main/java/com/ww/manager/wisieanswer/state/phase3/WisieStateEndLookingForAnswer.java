package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndLookingForAnswer extends WisieState {
    private Long interval;

    public WisieStateEndLookingForAnswer(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(MemberWisieStatus.LOOKING_FOR_ANSWER);
        double sumInterval = manager.getAnswerCount() * (2d - manager.getWarWisie().getSpeedF1()) * intervalMultiply() / 4;
        if (manager.getWarWisie().isHobby()) {
            sumInterval /= manager.getWarWisie().getHobbyFactor();
        }
        interval = (long) (sumInterval * (4 - 2 * manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getConcentrationF1()));
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
