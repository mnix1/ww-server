package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieLookingForAnswerState extends MemberWisieIntervalState {
    private boolean foundAnswer;
    private double chanceFoundAnswer;
    private double attributePart;
    private double random;

    public MemberWisieLookingForAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.LOOKING_FOR_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    protected double minInterval() {
        return manager.getContainer().getAnswerCount() * (1 - Math.max(Math.max(getWisie().getSpeedF1(), getWisie().getWisdomSum()), getWisie().getIntuitionF1())) / 2;
    }

    @Override
    protected double maxInterval() {
        return manager.getContainer().getAnswerCount() * (1 - Math.min(Math.min(getWisie().getSpeedF1(), getWisie().getWisdomSum()), getWisie().getIntuitionF1())) / 2;
    }

    @Override
    public void initProps() {
        super.initProps();
        attributePart = (getWisie().getWisdomSum() - 0.5) * 4 / 5;
        chanceFoundAnswer = 0.5 + manager.getContainer().difficultyPart(0.05) + attributePart + getWisie().getHobbyPart();
        random = randomDouble();
        foundAnswer = chanceFoundAnswer >= random;
    }

    @Override
    public String toString() {
        return super.toString() + ", foundAnswer=" + foundAnswer + ", chanceFoundAnswer=" + chanceFoundAnswer + ", attributePart=" + attributePart + ", random=" + random;
    }

    @Override
    public void after() {
        if (foundAnswer) {
            manager.getFlow().run("FOUND_ANSWER_LOOKING_FOR");
        } else {
            manager.getFlow().run("NO_FOUND_ANSWER_LOOKING_FOR");
        }
    }
}
