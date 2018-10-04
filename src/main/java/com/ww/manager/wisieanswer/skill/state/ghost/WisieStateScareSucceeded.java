package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateScareSucceeded extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateScareSucceeded.class);

    private WisieAnswerManager opponent;

    public WisieStateScareSucceeded(WisieAnswerManager manager, WisieAnswerManager opponent) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.SCARE_SUCCEEDED);
        opponent.addAction(WisieAnswerAction.RUN_AWAY);
        opponent.getTeam(opponent).getActiveTeamMember().addDisguise(DisguiseType.CHAIR_RED);
        manager.getManager().sendTeamAndActionsModel();
        long interval = (long) (randomDouble(2 - 2 * manager.getReflexF1(),
                4 - 4 * manager.getReflexF1()) * intervalMultiply());
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
