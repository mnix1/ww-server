package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotCaught extends WisieState {
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
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getManager().getModelFactory().fillModelWisieActions(m, wT);
            if(manager.getWisie().getProfile().getId().equals(warTeam.getProfileId())){
                warTeam.getTeamSkills().unblockAll();
            }
        });
        interval = (long) (randomDouble(3 - manager.getSpeedF1() - manager.getReflexF1() - manager.getConcentrationF1(),
                6 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - 2 * manager.getConcentrationF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
