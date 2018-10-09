package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateWasKidnapped extends WisieSkillState {
    public WisieStateWasKidnapped(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        //actions in WisieStateKidnappingSucceeded
//        warManager.addAndSendAction(WisieAnswerAction.WAS_KIDNAPPED);
    }
}
