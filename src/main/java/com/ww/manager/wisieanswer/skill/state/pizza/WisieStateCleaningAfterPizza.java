package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Getter
@Setter
public class WisieStateCleaningAfterPizza extends WisieState {
    private WisieAnswerManager opponentManager;
    private Long interval;

    public WisieStateCleaningAfterPizza(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    private long interval() {
        return (long) (randomDouble(6 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1(),
                10 - 3 * manager.getSpeedF1() - 3 * manager.getReflexF1()) * intervalMultiply());
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getTeam(opponentManager).getTeamSkills().unblockAll();
        manager.addAction(WisieAnswerAction.CLEANING_AFTER_PIZZA);
        opponentManager.getTeam(opponentManager).getActiveTeamMember().removeDisguise();
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = interval();
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
