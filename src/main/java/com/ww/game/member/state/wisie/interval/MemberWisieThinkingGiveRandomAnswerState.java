package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieThinkingGiveRandomAnswerState extends MemberWisieIntervalState {
    private boolean giveRandomAnswer;
    private double chanceGiveRandomAnswer;
    private double random;

    public MemberWisieThinkingGiveRandomAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING_GIVE_RANDOM_ANSWER);
    }

    @Override
    protected double minInterval() {
        return 1 - getWisie().getConfidenceF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - 3 * getWisie().getConfidenceF1();
    }


    private void init() {
        chanceGiveRandomAnswer = (getWisie().getConfidenceF1() + getWisie().getWisdomSum() + getWisie().getIntuitionF1()) / 3 + getWisie().getHobbyPart();
        random = randomDouble();
        giveRandomAnswer = chanceGiveRandomAnswer >= random;
    }

    @Override
    public String toString() {
        return super.toString() + ", giveRandomAnswer=" + giveRandomAnswer + ", chanceGiveRandomAnswer=" + chanceGiveRandomAnswer + ", random=" + random;
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    public void after() {
        if (giveRandomAnswer) {
            Map<String, Object> params = new HashMap<>();
            params.put("paramsPart", 2 * getWisie().getWisdomSum());
            manager.getFlow().run("ANSWERED", params);
        } else {
            manager.getFlow().run("SURRENDER");
        }
    }
}
