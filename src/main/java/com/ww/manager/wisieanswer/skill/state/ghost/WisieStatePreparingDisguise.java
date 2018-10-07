package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStatePreparingDisguise extends WisieState {
    private Long interval;

    public WisieStatePreparingDisguise(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe(){
        return super.describe() + ", interval=" + interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.PREPARING_DISGUISE);
        interval = (long) (randomDouble(2 - manager.getSpeedF1() - manager.getReflexF1(),
                4 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1()) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
