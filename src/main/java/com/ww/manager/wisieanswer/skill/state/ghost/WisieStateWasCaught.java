package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateWasCaught extends WisieState {
    public WisieStateWasCaught(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.addAction(WisieAnswerAction.WAS_CAUGHT);
        manager.getTeam(manager).activeTeamMemberOutDuringAnswering(DisguiseType.JUDGE);
        manager.getManager().sendActiveMemberAndActionsModel();
    }
}
