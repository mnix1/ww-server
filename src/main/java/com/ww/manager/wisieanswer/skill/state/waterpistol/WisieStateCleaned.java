package com.ww.manager.wisieanswer.skill.state.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateCleaned extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCleaned.class);

    public WisieStateCleaned(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    protected void processVoid() {
        manager.getTeam(manager).getActiveTeamMember().removeDisguise();
        manager.getManager().sendModel((m, wT) -> manager.getManager().getModelFactory().fillModelActiveMember(m, wT));
        logger.trace(describe());
    }
}
