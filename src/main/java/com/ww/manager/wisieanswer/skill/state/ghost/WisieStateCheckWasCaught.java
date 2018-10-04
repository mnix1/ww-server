package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckWasCaught extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCheckWasCaught.class);

    private boolean scareSuccess;

    public WisieStateCheckWasCaught(WisieAnswerManager manager, boolean scareSuccess) {
        super(manager, STATE_TYPE_DECISION);
        this.scareSuccess = scareSuccess;
    }

    @Override
    protected Boolean processBoolean() {
        double chance = (manager.getConfidenceF1() + manager.getIntuitionF1()) / 2;
        if (scareSuccess) {
            chance += 0.25;
        } else {
            chance -= 0.25;
        }
        boolean caught = chance <= randomDouble();
        logger.trace(manager.toString() + ", chance=" + chance+ ", caught=" + caught);
        return caught;
    }
}
