package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateScareSucceeded extends WisieSkillState {
    private WisieAnswerManager opponent;
    private Long interval;

    public WisieStateScareSucceeded(WisieAnswerManager manager, WisieAnswerManager opponent) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.SCARE_SUCCEEDED);
        opponent.addAction(WisieAnswerAction.RUN_AWAY);
        opponent.getTeam(opponent).activeTeamMemberOutDuringAnswering(DisguiseType.CHAIR_RED);
        manager.getWarManager().sendActiveMemberAndActionsModel();
        interval = (long) (randomDouble(2 - 2 * manager.getWarWisie().getReflexF1(),
                4 - 4 * manager.getWarWisie().getReflexF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
