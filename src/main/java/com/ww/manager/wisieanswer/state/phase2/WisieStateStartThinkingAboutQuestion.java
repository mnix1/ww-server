package com.ww.manager.wisieanswer.state.phase2;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateStartThinkingAboutQuestion extends WisieState {
    private Long interval;

    public WisieStateStartThinkingAboutQuestion(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }
    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINKING);
        double diffPart = (manager.getDifficulty() - 4) * 0.5;
        double doubleInterval = randomDouble(2.5 + diffPart - manager.getWarWisie().getWisdomSum(), 6.5 + diffPart - 5 * manager.getWarWisie().getWisdomSum());
        if (manager.getWarWisie().isHobby()) {
            doubleInterval /= manager.getWarWisie().getHobbyFactor();
        }
        interval = (long) (doubleInterval * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
