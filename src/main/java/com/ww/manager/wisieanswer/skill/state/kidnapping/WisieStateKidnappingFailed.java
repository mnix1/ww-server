package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateKidnappingFailed extends WisieSkillState {
    private WisieAnswerManager opponent;
    private Long interval;

    public WisieStateKidnappingFailed(WisieAnswerManager manager, WisieAnswerManager opponent ) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(MemberWisieStatus.KIDNAPPING_FAILED);
        opponent.addAction(MemberWisieStatus.WAS_NOT_KIDNAPPED);
        opponent.getTeam(opponent).getTeamSkills().unblockAll();
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (randomDouble(6 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - manager.getWarWisie().getConfidenceF1(),
                9 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1() - 2 * manager.getWarWisie().getConfidenceF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
