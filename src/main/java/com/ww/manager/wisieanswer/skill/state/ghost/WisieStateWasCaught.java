package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class WisieStateWasCaught extends WisieState {
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
        manager.getManager().sendActiveMemberAndActionsModel();
        interval = intervalMultiply() * 3;
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
