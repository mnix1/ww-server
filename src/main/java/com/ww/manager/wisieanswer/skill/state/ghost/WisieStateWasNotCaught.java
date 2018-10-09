package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotCaught extends WisieSkillState {
    private Long interval;

    public WisieStateWasNotCaught(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.WAS_NOT_CAUGHT);
        manager.getTeam(manager).getActiveTeamMember().removeDisguise();
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            manager.getWarManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getWarManager().getModelFactory().fillModelWisieActions(m, wT);
            if(manager.getOwnedWisie().getProfile().getId().equals(warTeam.getProfileId())){
                warTeam.getTeamSkills().unblockAll();
            }
        });
        interval = (long) (randomDouble(3 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConcentrationF1(),
                6 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConcentrationF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
