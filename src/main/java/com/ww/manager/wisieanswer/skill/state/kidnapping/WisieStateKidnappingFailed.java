package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateKidnappingFailed extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateKidnappingFailed.class);
    private WisieAnswerManager opponent;
    public WisieStateKidnappingFailed(WisieAnswerManager manager, WisieAnswerManager opponent ) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.KIDNAPPING_FAILED);
        opponent.addAction(WisieAnswerAction.WAS_NOT_KIDNAPPED);
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            manager.getManager().getModelFactory().fillModelWisieAnswering(m, wT);
            if(warTeam.getProfileId().equals(opponent.getWisie().getProfile().getId())){
                warTeam.getTeamSkills().unblockAll();
            }
        });
        long interval = (long) (randomDouble(6 - manager.getSpeedF1() - manager.getReflexF1() - manager.getConfidenceF1(),
                9 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - 2 * manager.getConfidenceF1()) * intervalMultiply());
        logger.trace(describe() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
