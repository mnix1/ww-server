package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieThinkingState extends MemberWisieIntervalState {
    private boolean thinkKnowAnswer;
    private double chanceKnowAnswer;
    private double difficultyPart;
    private double attributePart;

    public MemberWisieThinkingState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING);
    }

    @Override
    protected double minInterval() {
        return 1.75 + (manager.getContainer().getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum();
    }

    @Override
    protected double maxInterval() {
        return 5.75 + (manager.getContainer().getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum() * 5;
    }

    private void init() {
        difficultyPart = (4 - manager.getContainer().getDifficulty()) * 0.05;
        attributePart = ((getWisie().getWisdomSum() + getWisie().getConfidenceF1()) / 2 - 0.5) * 4 / 5;
        chanceKnowAnswer = 0.5 + difficultyPart + attributePart + getWisie().getHobbyPart();
        thinkKnowAnswer = chanceKnowAnswer >= randomDouble();
    }

    @Override
    public String toString() {
        return super.toString() + ", thinkKnowAnswer=" + thinkKnowAnswer + ", chanceKnowAnswer=" + chanceKnowAnswer + ", difficultyPart=" + difficultyPart + ", attributePart=" + attributePart;
    }

    @Override
    public void execute() {
        init();
        super.execute();
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
