package com.ww.manager.wisieanswer.skill.state.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;

public class WisieStateCleaned extends WisieState {
    public WisieStateCleaned(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getTeam(manager).getActiveTeamMember().removeDisguise();
        manager.getWarManager().sendModel((m, wT) -> manager.getWarManager().getModelFactory().fillModelActiveMemberAddOn(m, wT));
    }
}
