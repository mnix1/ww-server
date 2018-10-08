package com.ww.manager.wisieanswer.state.multiphase;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateLostConcentration extends WisieState {
    private WisieAnswerAction noConcentrationAction;
    private Long interval;

    public WisieStateLostConcentration(WisieAnswerManager manager, WisieAnswerAction noConcentrationAction) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.noConcentrationAction = noConcentrationAction;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval + ", noConcentrationAction=" + noConcentrationAction;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(noConcentrationAction);
        interval = (long) (randomDouble(2 - manager.getWarWisie().getConcentrationF1(), 5 - manager.getWarWisie().getConcentrationF1() * 4) * intervalMultiply());
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
