package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateScareFailed extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateScareFailed.class);

    private WisieAnswerManager opponent;

    public WisieStateScareFailed(WisieAnswerManager manager, WisieAnswerManager opponent) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.SCARE_FAILED);
        opponent.addAction(WisieAnswerAction.WAS_NOT_SCARED);
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            manager.getManager().getModelFactory().fillModelWisieAnswering(m, wT);
            if(opponent.getWisie().getProfile().getId().equals(warTeam.getProfileId())){
                warTeam.getTeamSkills().unblockAll();
            }
        });
        long interval = (long) (randomDouble(6 - 6 * manager.getReflexF1(),
                8 - 8 * manager.getReflexF1()) * intervalMultiply());
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
