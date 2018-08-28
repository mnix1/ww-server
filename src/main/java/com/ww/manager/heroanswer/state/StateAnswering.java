package com.ww.manager.heroanswer.state;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.entity.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateAnswering extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnswering.class);

    public StateAnswering(HeroAnswerManager manager) {
        super(manager);
    }

    protected void processVoid() {
        if (!manager.isInProgress()) {
            return;
        }
        manager.addAndSendAction(HeroAnswerAction.ANSWERED);
        double sumWisdomAttributeF2 = manager.sumWisdomAttributeF2();
        boolean correctAnswer = sumWisdomAttributeF2 > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", sumWisdomAttributeF2: " + sumWisdomAttributeF2 + ", correctAnswer: " + correctAnswer);
        Answer answer = manager.getQuestion().getAnswers().stream().filter(a -> a.getCorrect().equals(correctAnswer)).findFirst().get();
        manager.getWarManager().heroAnswered(manager.getHero().getProfile().getId(), answer.getId());
    }
}
