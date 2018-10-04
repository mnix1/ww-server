package com.ww.manager.wisieanswer.skill.state.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWaterPistolUsedOnIt extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateWaterPistolUsedOnIt.class);

    public WisieStateWaterPistolUsedOnIt(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.WATER_PISTOL_USED_ON_IT);
        manager.getTeam(manager).getActiveTeamMember().changeWisieDisguise(DisguiseType.RAIN);
        manager.getManager().sendTeamAndActionsModel();
        long interval = (long) (randomDouble(6 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - manager.getConcentrationF1() - manager.getConfidenceF1(),
                8 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - 2 * manager.getConcentrationF1() - 2 * manager.getConfidenceF1()) * intervalMultiply());
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
