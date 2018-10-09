package com.ww.manager.wisieanswer.skill.state.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;

public class WisieStateCleaned extends WisieSkillState {
    public WisieStateCleaned(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getWisieMember().removeDisguise();
        manager.getWarManager().sendModel((m, wT) -> manager.getModelFactory().fillModelActiveMemberAddOn(m, wT));
    }
}
