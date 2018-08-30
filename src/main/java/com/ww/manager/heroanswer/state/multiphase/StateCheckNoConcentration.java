package com.ww.manager.heroanswer.state.multiphase;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateCheckNoConcentration extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckNoConcentration.class);

    public StateCheckNoConcentration(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        boolean lostConcentration = manager.getConcentrationF1() < randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", lostConcentration: " + lostConcentration);
        if (lostConcentration) {
            return randomElement(HeroAnswerAction.getNoConcentrationActions());
        }
        return HeroAnswerAction.NONE;
    }
}
