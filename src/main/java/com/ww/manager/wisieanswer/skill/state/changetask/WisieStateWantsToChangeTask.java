package com.ww.manager.wisieanswer.skill.state.changetask;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWantsToChangeTask extends WisieSkillState {
    private Long interval;

    public WisieStateWantsToChangeTask(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.getWisieMember().addDisguise(DisguiseType.BIRD_RED);
        manager.addAction(MemberWisieStatus.WANTS_TO_CHANGE_TASK);
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (randomDouble(4 - manager.getWarWisie().getReflexF1() - 3 * manager.getWarWisie().getCunningF1(),
                6 - manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getReflexF1() - 4 * manager.getWarWisie().getCunningF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
