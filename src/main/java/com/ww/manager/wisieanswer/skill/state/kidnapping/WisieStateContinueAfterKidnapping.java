package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.container.rival.war.WarTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateContinueAfterKidnapping extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(com.ww.manager.wisieanswer.skill.state.ghost.WisieStateRemovingDisguise.class);

    public WisieStateContinueAfterKidnapping(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getTeam(manager).getActiveTeamMember().removeDisguise();
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            if (warTeam.getProfileId().equals(manager.getWisie().getProfile().getId())) {
                warTeam.getTeamSkills().unblockAll();
            }
        });
        logger.trace(describe());
    }
}
