package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieThinkingState extends MemberWisieIntervalState {
    private boolean thinkKnowAnswer;
    private double chanceKnowAnswer;
    private double attributePart;
    private double random;

    public MemberWisieThinkingState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING);
    }

    @Override
    protected double minInterval() {
        double difficultyPart = manager.getContainer().getDifficulty() * (1 - Math.max(getWisie().getWisdomSum(), getWisie().getConfidenceF1()));
        return 1 - getWisie().getWisdomSum() + difficultyPart;
    }

    @Override
    protected double maxInterval() {
        double difficultyPart = manager.getContainer().getDifficulty() * (1 - Math.max(getWisie().getWisdomSum(), getWisie().getConfidenceF1()));
        return 2 - getWisie().getWisdomSum() - getWisie().getConfidenceF1() + difficultyPart;
    }

    @Override
    public void initProps() {
        super.initProps();
        attributePart = ((getWisie().getWisdomSum() + getWisie().getConfidenceF1()) / 2 - 0.5) * 4 / 5;
        chanceKnowAnswer = 0.5 + manager.getContainer().difficultyPart(0.05) + attributePart + getWisie().getHobbyPart();
        random = randomDouble();
        thinkKnowAnswer = chanceKnowAnswer >= random;
    }

    @Override
    public String toString() {
        return super.toString() + ", thinkKnowAnswer=" + thinkKnowAnswer + ", chanceKnowAnswer=" + chanceKnowAnswer + ", attributePart=" + attributePart + ", random=" + random;
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        if (thinkKnowAnswer) {
            manager.getFlow().run("THINK_KNOW_ANSWER");
        } else {
            manager.getFlow().run("NOT_SURE_OF_ANSWER");
        }
    }
}
