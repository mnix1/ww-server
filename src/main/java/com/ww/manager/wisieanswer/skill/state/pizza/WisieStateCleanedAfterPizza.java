package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WisieStateCleanedAfterPizza extends WisieSkillState {
    public WisieStateCleanedAfterPizza(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getTeam(manager).getTeamSkills().unblockAll();
        manager.getWisieMember().removeDisguise();
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getModelFactory().fillModelWisieActions(m, wT);
        });
    }
}
