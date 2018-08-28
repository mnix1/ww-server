package com.ww.manager.heroanswer.state;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndThinking extends State{
    protected static final Logger logger = LoggerFactory.getLogger(StateEndThinking.class);

    public StateEndThinking(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        double sumWisdomAttributeF2 = manager.sumWisdomAttributeF2();
        boolean thinkKnowAnswer = sumWisdomAttributeF2 > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", sumWisdomAttributeF2: " + sumWisdomAttributeF2 +", thinkKnowAnswer: " + thinkKnowAnswer);
        if (thinkKnowAnswer) {
            return HeroAnswerAction.THINK_KNOW_ANSWER;
//            boolean knowAnswer = sumWisdomAttributeF2 > randomDouble();
//            if (knowAnswer) {
//
//            }
        }
        return HeroAnswerAction.ANSWERED;
    }
}
