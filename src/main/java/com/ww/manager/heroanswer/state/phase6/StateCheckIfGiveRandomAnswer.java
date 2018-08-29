package com.ww.manager.heroanswer.state.phase6;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckIfGiveRandomAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckIfGiveRandomAnswer.class);

    public StateCheckIfGiveRandomAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        double intuitionF2 = manager.f1(manager.getHero().getMentalAttributeIntuition());
        double confidenceF2 = manager.f1(manager.getHero().getMentalAttributeConfidence());
        boolean willGiveRandomAnswer = intuitionF2 + confidenceF2 > randomDouble() * 2;
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name()
                + ", intuitionF1: " + intuitionF2
                + ", confidenceF1: " + confidenceF2
                + ", willGiveRandomAnswer: " + willGiveRandomAnswer);
        if (willGiveRandomAnswer) {
            return HeroAnswerAction.WILL_GIVE_RANDOM_ANSWER;
        }
        return HeroAnswerAction.WONT_GIVE_RANDOM_ANSWER;
    }
}
