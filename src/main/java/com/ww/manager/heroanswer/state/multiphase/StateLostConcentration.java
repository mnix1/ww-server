package com.ww.manager.heroanswer.state.multiphase;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateLostConcentration extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateLostConcentration.class);

    private HeroAnswerAction noConcentrationAction;

    public StateLostConcentration(HeroAnswerManager manager, HeroAnswerAction noConcentrationAction) {
        super(manager);
        this.noConcentrationAction = noConcentrationAction;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(noConcentrationAction);
        long interval = (long) (randomDouble(2 - manager.getConcentrationF1(), 5 - manager.getConcentrationF1() * 4) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
