package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class WisieStateKidnappingSucceeded extends WisieSkillState {
    private WisieAnswerManager opponent;
    private Long interval;

    public WisieStateKidnappingSucceeded(WisieAnswerManager manager, WisieAnswerManager opponent) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(MemberWisieStatus.KIDNAPPING_SUCCEEDED);
        opponent.addAction(MemberWisieStatus.WAS_KIDNAPPED);
        manager.getTeam(opponent).activeTeamMemberOutDuringAnswering(DisguiseType.CHAIR_SIMPLE);
        manager.getTeam(manager).activeTeamMemberOutDuringAnswering(DisguiseType.CHAIR_SIMPLE);
        manager.getWarManager().sendActiveMemberAndActionsModel();
        interval = intervalMultiply() * 3;
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
