package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import io.reactivex.Flowable;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateServesPizza extends WisieSkillState {
    private WisieAnswerManager opponentManager;
    private Long interval;

    public WisieStateServesPizza(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    private long interval() {
        return (long) (randomDouble(4 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1(),
                6 - 3 * manager.getWarWisie().getSpeedF1() - 3 * manager.getWarWisie().getReflexF1()) * intervalMultiply());
    }
    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(MemberWisieStatus.SERVING_PIZZA);
        opponentManager.addAction(MemberWisieStatus.PREPARING_FOR_EAT_PIZZA);
        opponentManager.getTeam(opponentManager).getActiveTeamMember().addDisguise(DisguiseType.PIZZA_MAN);
        manager.getWarManager().sendActiveMemberAndActionsModel();
        interval = interval();
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
