package com.ww.manager.heroanswer.state.phase5;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckKnowAnswerAfterThinkingWhichMatch extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckKnowAnswerAfterThinkingWhichMatch.class);

    public StateCheckKnowAnswerAfterThinkingWhichMatch(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        double sumWisdomAttributeF1 = manager.sumWisdomAttributeF1();
        double intuitionF1 = manager.f1(manager.getHero().getMentalAttributeIntuition());
        double confidenceF1 = manager.f1(manager.getHero().getMentalAttributeConfidence());
        boolean thinkKnowAnswer = sumWisdomAttributeF1 + intuitionF1 + confidenceF1 > randomDouble() * 3;
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name()
                + ", sumWisdomAttributeF1: " + sumWisdomAttributeF1
                + ", intuitionF1: " + intuitionF1
                + ", confidenceF1: " + confidenceF1
                + ", thinkKnowAnswer: " + thinkKnowAnswer);
        if (thinkKnowAnswer) {
            return HeroAnswerAction.THINK_KNOW_ANSWER;
        }
        return HeroAnswerAction.NOT_SURE_OF_ANSWER;
    }
}
