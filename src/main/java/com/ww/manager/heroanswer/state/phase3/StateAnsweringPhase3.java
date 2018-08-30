package com.ww.manager.heroanswer.state.phase3;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.entity.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateAnsweringPhase3 extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnsweringPhase3.class);

    public StateAnsweringPhase3(HeroAnswerManager manager) {
        super(manager);
    }

    protected void processVoid() {
        manager.addAndSendAction(HeroAnswerAction.ANSWERED);
        double diffPart = (4 - manager.getDifficulty()) * 0.1;
        double attrPart = ((2 * manager.getWisdomSum() + manager.getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean correctAnswer = chance > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", chance: " + chance + ", correctAnswer: " + correctAnswer);
        Answer answer = correctAnswer
                ? manager.getQuestion().getAnswers().stream().filter(Answer::getCorrect).findFirst().get()
                : randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        manager.getWarManager().heroAnswered(manager.getHero().getProfile().getId(), answer.getId());
    }
}
