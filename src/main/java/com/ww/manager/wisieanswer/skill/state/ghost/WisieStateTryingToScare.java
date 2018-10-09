package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateTryingToScare extends WisieSkillState {
    private WisieAnswerManager opponentManager;
    private boolean success;
    private long interval;

    public WisieStateTryingToScare(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    public boolean calculateSuccess() {
        double value = manager.getWarWisie().getIntuitionF1() + manager.getWarWisie().getConfidenceF1();
        double opponentValue = opponentManager.getWarWisie().getIntuitionF1() + opponentManager.getWarWisie().getConfidenceF1();
        return randomDouble(value, 2 * value) + value > randomDouble(opponentValue, 2 * opponentValue) + opponentValue;
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
        manager.getWarManager().sendNewSkillsModel((m, wT) -> {
            manager.getWarManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getWarManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (intervalMultiply() * (3d - Math.abs(manager.getWarWisie().getIntuitionF1() - opponentManager.getWarWisie().getIntuitionF1())
                - Math.abs(manager.getWarWisie().getSpeedF1() - opponentManager.getWarWisie().getSpeedF1())
                - Math.abs(manager.getWarWisie().getConfidenceF1() - opponentManager.getWarWisie().getConfidenceF1())));
        opponentManager.getFlow().getGhostSkillFlow().ghostUsedOnIt(success, interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
