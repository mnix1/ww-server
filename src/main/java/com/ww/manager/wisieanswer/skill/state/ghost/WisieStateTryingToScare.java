package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateTryingToScare extends WisieState {
    private WisieAnswerManager opponentManager;
    private boolean success;
    private long interval;

    public WisieStateTryingToScare(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    public boolean calculateSuccess() {
        double value = manager.getIntuitionF1() + manager.getConfidenceF1();
        double opponentValue = opponentManager.getIntuitionF1() + opponentManager.getConfidenceF1();
        return randomDouble(value, 2 * value) + value > randomDouble(opponentValue, 2 * opponentValue) + opponentValue;
    }

    public long calculateInterval() {
        return (long) (intervalMultiply() * (3d - Math.abs(manager.getIntuitionF1() - opponentManager.getIntuitionF1())
                - Math.abs(manager.getSpeedF1() - opponentManager.getSpeedF1())
                - Math.abs(manager.getConfidenceF1() - opponentManager.getConfidenceF1())));
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval + ", success=" + success;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getTeam(manager).getTeamSkills().blockAll();
        manager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.addAction(WisieAnswerAction.TRYING_TO_SCARE);
        manager.getTeam(manager).getActiveTeamMember().addDisguise(DisguiseType.GHOST);
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        opponentManager.getFlow().getGhostSkillFlow().ghostUsedOnIt(success, interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
