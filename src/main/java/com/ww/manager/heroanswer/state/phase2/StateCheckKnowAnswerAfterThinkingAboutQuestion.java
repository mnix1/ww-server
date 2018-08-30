package com.ww.manager.heroanswer.state.phase2;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckKnowAnswerAfterThinkingAboutQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckKnowAnswerAfterThinkingAboutQuestion.class);

    public StateCheckKnowAnswerAfterThinkingAboutQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWisdomSum() + manager.getConfidenceF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean thinkKnowAnswer = chance > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", chance: " + chance+ ", thinkKnowAnswer: " + thinkKnowAnswer);
        if (thinkKnowAnswer) {
            return HeroAnswerAction.THINK_KNOW_ANSWER;
        }
        return HeroAnswerAction.NOT_SURE_OF_ANSWER;
    }
}
