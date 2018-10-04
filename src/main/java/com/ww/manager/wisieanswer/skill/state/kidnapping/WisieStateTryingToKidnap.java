package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateTryingToKidnap extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateTryingToKidnap.class);

    private WisieAnswerManager opponentManager;
    private boolean success;
    private long interval;

    public WisieStateTryingToKidnap(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    public boolean calculateSuccess() {
        double value = manager.getWisdomSum() + manager.getConfidenceF1();
        double opponentValue = opponentManager.getWisdomSum() + opponentManager.getConfidenceF1();
        return value > opponentValue
                || randomDouble(value, 2 * value) + value > randomDouble(opponentValue, 2 * opponentValue) + opponentValue;
    }

    public long calculateInterval() {
        return (long) (intervalMultiply() * (3d - Math.abs(manager.getWisdomSum() - opponentManager.getWisdomSum())
                - Math.abs(manager.getSpeedF1() - opponentManager.getSpeedF1())
                - Math.abs(manager.getConfidenceF1() - opponentManager.getConfidenceF1())));
    }

    protected Flowable<Long> processFlowable() {
        manager.getTeam(manager).getTeamSkills().blockAll();
        manager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.getManager().sendNewSkillsModel();
        manager.addAndSendAction(WisieAnswerAction.TRYING_TO_KIDNAP);
        opponentManager.getFlow().getKidnappingSkillFlow().kidnappingUsedOnIt(success, interval);
        logger.trace(manager.toString() + ", interval=" + interval + ", success=" + success);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
