package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndThinkingIfGiveRandomAnswer extends WisieState {
    private Long interval;

    public WisieStateEndThinkingIfGiveRandomAnswer(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINKING_IF_GIVE_RANDOM_ANSWER);
        double sumInterval = randomDouble(1 - manager.getWarWisie().getConfidenceF1(), 4 - 2 * manager.getWarWisie().getConfidenceF1() - manager.getWarWisie().getIntuitionF1());
        if (manager.getWarWisie().isHobby()) {
            sumInterval /= manager.getWarWisie().getHobbyFactor();
        }
        interval = (long) (sumInterval * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
