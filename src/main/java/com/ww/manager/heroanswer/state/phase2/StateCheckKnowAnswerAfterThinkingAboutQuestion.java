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
        double sumWisdomAttributeF2 = manager.sumWisdomAttributeF2();
        boolean thinkKnowAnswer = sumWisdomAttributeF2 > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", sumWisdomAttributeF2: " + sumWisdomAttributeF2 + ", thinkKnowAnswer: " + thinkKnowAnswer);
        if (thinkKnowAnswer) {
            return HeroAnswerAction.THINK_KNOW_ANSWER;
        }
        return HeroAnswerAction.NOT_SURE_OF_ANSWER;
    }
}
