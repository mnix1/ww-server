package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class WisieStateWasCaught extends WisieSkillState {
    private Long interval;

    public WisieStateWasCaught(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }


    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.WAS_CAUGHT);
        manager.getTeam(manager).activeTeamMemberOutDuringAnswering(DisguiseType.JUDGE);
        manager.getWarManager().sendActiveMemberAndActionsModel();
        interval = intervalMultiply() * 3;
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
