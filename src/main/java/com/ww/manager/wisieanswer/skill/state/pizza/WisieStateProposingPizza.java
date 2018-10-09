package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Getter
@Setter
public class WisieStateProposingPizza extends WisieSkillState {
    private WisieAnswerManager opponentManager;
    private Long interval;

    public WisieStateProposingPizza(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    private long interval() {
        return (long) (randomDouble(5 - 2 * manager.getWarWisie().getSpeedF1() - 2 * manager.getWarWisie().getReflexF1(),
                7 - 3 * manager.getWarWisie().getSpeedF1() - 3 * manager.getWarWisie().getReflexF1()) * intervalMultiply());
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getTeam(manager).getTeamSkills().blockAll();
        manager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.addAction(WisieAnswerAction.PROPOSING_PIZZA);
        opponentManager.addAction(WisieAnswerAction.THINKING_IF_GET_PIZZA);
        manager.getTeam(manager).getActiveTeamMember().addDisguise(DisguiseType.PIZZA_COOK);
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getWarManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getWarManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = interval();
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
