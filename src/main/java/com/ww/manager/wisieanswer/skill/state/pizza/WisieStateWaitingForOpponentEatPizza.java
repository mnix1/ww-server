package com.ww.manager.wisieanswer.skill.state.pizza;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Setter
public class WisieStateWaitingForOpponentEatPizza extends WisieState {
    private WisieAnswerManager opponentManager;
    private Long interval;

    public WisieStateWaitingForOpponentEatPizza(WisieAnswerManager manager, WisieAnswerManager opponentManager) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponentManager = opponentManager;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    private long interval() {
        return (long) (randomDouble(3,
                6) * intervalMultiply());
    }


    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.WAITING_FOR_OPPONENT_EAT_PIZZA);
        opponentManager.addAndSendAction(WisieAnswerAction.EATING_PIZZA);
        interval = interval();
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
