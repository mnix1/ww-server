package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateTryingToKidnap extends WisieState {
    private WisieAnswerManager opponentManager;
    private boolean success;
    private long interval;

    public WisieStateTryingToKidnap(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    public boolean calculateSuccess() {
        double value = manager.getWisdomSum() + manager.getConfidenceF1();
        double opponentValue = opponentManager.getWisdomSum() + opponentManager.getConfidenceF1();
        return value > opponentValue
                || randomDouble(value, 2 * value) + value > randomDouble(opponentValue, 2 * opponentValue) + opponentValue;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval + ", success=" + success;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getTeam(manager).getTeamSkills().blockAll();
        manager.getTeam(opponentManager).getTeamSkills().blockAll();
        manager.addAction(WisieAnswerAction.TRYING_TO_KIDNAP);
        manager.getTeam(manager).getActiveTeamMember().addDisguise(DisguiseType.NINJA);
        manager.getManager().sendNewSkillsModel((m, wT) -> {
            manager.getManager().getModelFactory().fillModelActiveMemberAddOn(m, wT);
            manager.getManager().getModelFactory().fillModelWisieActions(m, wT);
        });
        interval = (long) (intervalMultiply() * (3d - Math.abs(manager.getWisdomSum() - opponentManager.getWisdomSum())
                - Math.abs(manager.getSpeedF1() - opponentManager.getSpeedF1())
                - Math.abs(manager.getConfidenceF1() - opponentManager.getConfidenceF1())));
        opponentManager.getFlow().getKidnappingSkillFlow().kidnappingUsedOnIt(success, interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
