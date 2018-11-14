package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieLookingForAnswerState extends MemberWisieIntervalState {
    private boolean foundAnswer;
    private double chanceFoundAnswer;
    private double difficultyPart;
    private double attributePart;

    public MemberWisieLookingForAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.LOOKING_FOR_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        double sumInterval = manager.getContainer().getAnswerCount() * (2 - getWisie().getSpeedF1() - getWisie().getWisdomSum());
        return hobbyImpact(sumInterval * (5 - getWisie().getSpeedF1() - getWisie().getIntuitionF1() - getWisie().getCunningF1() - getWisie().getWisdomSum()));
    }

    private void init() {
        difficultyPart = (4 - manager.getContainer().getDifficulty()) * 0.05;
        attributePart = (getWisie().getWisdomSum() - 0.5) * 4 / 5;
        chanceFoundAnswer = 0.5 + difficultyPart + attributePart + getWisie().getHobbyPart();
        foundAnswer = chanceFoundAnswer >= randomDouble();
    }

    @Override
    public String toString() {
        return super.toString() + ", foundAnswer=" + foundAnswer + ", chanceFoundAnswer=" + chanceFoundAnswer + ", difficultyPart=" + difficultyPart + ", attributePart=" + attributePart;
    }

    @Override
    public void execute() {
        init();
        super.execute();
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
