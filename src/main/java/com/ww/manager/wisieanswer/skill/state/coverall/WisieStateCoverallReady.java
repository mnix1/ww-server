package com.ww.manager.wisieanswer.skill.state.coverall;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCoverallReady extends WisieSkillState {
    private Long interval;
    private WisieAnswerManager opponentManager;

    public WisieStateCoverallReady(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    private void maybeRunOpponentPrevState() {
        AbstractState state = opponentManager.getFlow().lastFlowableState();
        if (state instanceof WisieSkillState) {
            state.dispose();
            ((WisieSkillState) state).runPrevState();
        }
    }

    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.COVERALL_READY);
        opponentManager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.getWisieMember().addDisguise(DisguiseType.COVERALL);
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getModelFactory().fillModelWisieActions(m, wT);
        });
        maybeRunOpponentPrevState();
        interval = (long) (randomDouble(2 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1(),
                4 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
