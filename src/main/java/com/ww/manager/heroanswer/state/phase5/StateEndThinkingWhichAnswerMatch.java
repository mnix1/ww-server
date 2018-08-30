package com.ww.manager.heroanswer.state.phase5;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndThinkingWhichAnswerMatch extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndThinkingWhichAnswerMatch.class);

    public StateEndThinkingWhichAnswerMatch(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable(){
        manager.addAndSendAction(HeroAnswerAction.THINKING_WHICH_ANSWER_MATCH);
        double sumInterval = manager.getAnswerCount() * (4d - manager.getWisdomSum() - manager.getIntuitionF1()) * 100;
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * (3 - manager.getWisdomSum() - manager.getIntuitionF1()));
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
