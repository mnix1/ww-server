package com.ww.manager.wisieanswer.state.phase4;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndRecognizingAnswers extends WisieState {
    private Long interval;

    public WisieStateEndRecognizingAnswers(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(MemberWisieStatus.RECOGNIZING_ANSWERS);
        double sumInterval = manager.getAnswerCount() * (3d - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getConcentrationF1()) * 500;
        if (manager.getWarWisie().isHobby()) {
            sumInterval /= manager.getWarWisie().getHobbyFactor();
        }
        interval = (long) (sumInterval * (3 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getConcentrationF1()));
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
