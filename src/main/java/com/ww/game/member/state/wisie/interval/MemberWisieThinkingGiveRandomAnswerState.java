package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieThinkingGiveRandomAnswerState extends MemberWisieIntervalState {
    private boolean giveRandomAnswer;
    private double chanceGiveRandomAnswer;
    private double difficultyPart;
    private double attributePart;

    public MemberWisieThinkingGiveRandomAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING_GIVE_RANDOM_ANSWER);
    }

    @Override
    protected double minInterval() {
        return 1 - getWisie().getConfidenceF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - getWisie().getReflexF1() - getWisie().getConcentrationF1() - getWisie().getConfidenceF1();
    }


    private void init() {
        difficultyPart = (4 - manager.getContainer().getDifficulty()) * 0.15;
        attributePart = ((2 * getWisie().getConfidenceF1() + getWisie().getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        chanceGiveRandomAnswer = 0.5 + difficultyPart + attributePart + getWisie().getHobbyPart();
        giveRandomAnswer = chanceGiveRandomAnswer >= randomDouble();
    }

    @Override
    public String toString() {
        return super.toString() + ", giveRandomAnswer=" + giveRandomAnswer + ", chanceGiveRandomAnswer=" + chanceGiveRandomAnswer + ", difficultyPart=" + difficultyPart + ", attributePart=" + attributePart;
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
            params.put("paramsPart", 2 * getWisie().getIntuitionF1());
            manager.getFlow().run("ANSWERED", params);
        } else {
            manager.getFlow().run("SURRENDER");
        }
    }
}
