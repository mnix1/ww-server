package com.ww.manager.heroanswer.state.phase3;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckFoundAnswerLookingFor extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckFoundAnswerLookingFor.class);

    public StateCheckFoundAnswerLookingFor(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = (manager.getWisdomSum() - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean foundAnswerLookingFor = chance > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", foundAnswerLookingFor: " + foundAnswerLookingFor);
        if (foundAnswerLookingFor) {
            return HeroAnswerAction.FOUND_ANSWER_LOOKING_FOR;
        }
        return HeroAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR;
    }
}
