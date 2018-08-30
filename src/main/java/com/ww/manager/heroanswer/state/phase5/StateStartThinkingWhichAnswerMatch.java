package com.ww.manager.heroanswer.state.phase5;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartThinkingWhichAnswerMatch extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartThinkingWhichAnswerMatch.class);
    private double getMinInterval() {
        return manager.getQuestion().getAnswers().size() * 0.3;
    }

    public StateStartThinkingWhichAnswerMatch(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable(){
        manager.addAndSendAction(HeroAnswerAction.THINKING_WHICH_ANSWER_MATCH);
        double intuitionF1 = manager.f1(manager.getHero().getMentalAttributeIntuition());
        double sumWisdomAttributeF1 = manager.getWisdomSum();
        double minInterval = getMinInterval();
        long interval = (long) (randomDouble(minInterval, (5 - 2 * intuitionF1 - 2 * sumWisdomAttributeF1) * minInterval) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", intuitionF1: " + intuitionF1 + ", sumWisdom: " + sumWisdomAttributeF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
