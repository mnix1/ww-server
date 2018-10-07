package com.ww.manager.rival.war.skill.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

public class WarStateHintUsed extends WarState {
    private Long profileId;
    private Map<String, Object> content;

    public WarStateHintUsed(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected void processVoid() {
        WarTeam team = manager.getModel().getTeams().team(profileId);
        if (!team.getTeamSkills().canUse(Skill.HINT) || !content.containsKey("answerId")
                || !team.getActiveTeamMember().isWisie() || !team.istActiveTeamMemberPresent()) {
            return;
        }
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(profileId);
        team.getTeamSkills().use(Skill.HINT).disable();
        Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
        Boolean isAnswerCorrect = manager.getModel().findCurrentCorrectAnswerId().equals(markedAnswerId);
        wisieAnswerManager.getFlow().getHintSkillFlow().hint(markedAnswerId, isAnswerCorrect);
        manager.sendNewSkillsModel();
    }
}
