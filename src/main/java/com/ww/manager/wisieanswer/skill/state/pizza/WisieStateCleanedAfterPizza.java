package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Getter
@Setter
public class WisieStateCleanedAfterPizza extends WisieState {
    public WisieStateCleanedAfterPizza(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getTeam(manager).getTeamSkills().unblockAll();
        manager.getTeam(manager).getActiveTeamMember().removeDisguise();
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getManager().getModelFactory().fillModelWisieActions(m, wT);
        });
    }
}
