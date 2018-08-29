package com.ww.manager.heroanswer.state.phase6;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.entity.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElementIndex;

public class StateAnsweringPhase6 extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnsweringPhase6.class);

    public StateAnsweringPhase6(HeroAnswerManager manager) {
        super(manager);
    }

    protected void processVoid() {
        manager.addAndSendAction(HeroAnswerAction.ANSWERED);
        Answer answer = randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", correctAnswer: " + answer.getCorrect());
        manager.getWarManager().heroAnswered(manager.getHero().getProfile().getId(), answer.getId());
    }
}
