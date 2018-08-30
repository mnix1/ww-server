package com.ww.manager.heroanswer.state.phase2;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartThinkingAboutQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartThinkingAboutQuestion.class);

    public StateStartThinkingAboutQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.THINKING);
        double doubleInterval = randomDouble(manager.getDifficulty() - manager.getWisdomSum(), 2 * manager.getDifficulty() - 2 * manager.getWisdomSum());
        if (manager.isHobby()) {
            doubleInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (doubleInterval * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
