package com.ww.manager.wisieanswer.state.phase5;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndThinkingWhichAnswerMatch extends WisieState {
    private Long interval;

    public WisieStateEndThinkingWhichAnswerMatch(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable(){
        manager.addAndSendAction(WisieAnswerAction.THINKING_WHICH_ANSWER_MATCH);
        double sumInterval = manager.getAnswerCount() * (4d - manager.getWisdomSum() - manager.getIntuitionF1()) * 100;
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        interval = (long) (sumInterval * (3d - manager.getWisdomSum() - manager.getIntuitionF1()));
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
