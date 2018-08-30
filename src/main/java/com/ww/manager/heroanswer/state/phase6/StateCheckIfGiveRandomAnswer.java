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
        double diffPart = (4 - manager.getDifficulty()) * 0.15;
        double attrPart = ((2 * manager.getConfidenceF1() + manager.getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean willGiveRandomAnswer = chance > randomDouble();
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", chance: " + chance + ", willGiveRandomAnswer: " + willGiveRandomAnswer);
        if (willGiveRandomAnswer) {
            return HeroAnswerAction.WILL_GIVE_RANDOM_ANSWER;
        }
        return HeroAnswerAction.WONT_GIVE_RANDOM_ANSWER;
    }
}
