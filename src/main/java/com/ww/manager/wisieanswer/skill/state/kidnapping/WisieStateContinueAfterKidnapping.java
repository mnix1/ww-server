package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.container.rival.war.WarTeam;

public class WisieStateContinueAfterKidnapping extends WisieSkillState {

    public WisieStateContinueAfterKidnapping(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getWisieMember().removeDisguise();
        manager.getTeam(manager).getTeamSkills().unblockAll();
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelActiveMemberAddOn(m, wT);
        });
    }
}
