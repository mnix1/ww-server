package com.ww.manager.wisieanswer.skill.state.coverall;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCoverallReady extends WisieState {
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

    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.COVERALL_READY);
        opponentManager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.getTeam(manager).getActiveTeamMember().addDisguise(DisguiseType.COVERALL);
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getWarManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getWarManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (randomDouble(2 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1(),
                4 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
