package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.random;
import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieThinkingWhichAnswerMatchState extends MemberWisieIntervalState {
    private boolean thinkKnowAnswer;
    private double chanceKnowAnswer;
    private double attributePart;
    private double random;

    public MemberWisieThinkingWhichAnswerMatchState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING_WHICH_ANSWER_MATCH);
    }

    private void init() {
        attributePart = ((getWisie().getWisdomSum() + getWisie().getIntuitionF1() + getWisie().getConfidenceF1()) / 3 - 0.5) * 4 / 5;
        chanceKnowAnswer = 0.5 + manager.getContainer().difficultyPart(0.05) + attributePart + getWisie().getHobbyPart();
        random = randomDouble();
        thinkKnowAnswer = chanceKnowAnswer >= random;
    }

    @Override
    public String toString() {
        return super.toString() + ", thinkKnowAnswer=" + thinkKnowAnswer + ", chanceKnowAnswer=" + chanceKnowAnswer + ", attributePart=" + attributePart + ", random=" + random;
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    protected double prepareInterval() {
        double oneAnswerInterval;
        TaskRenderer taskRenderer = manager.getContainer().getQuestion().getType().getAnswerRenderer();
        if (taskRenderer == TaskRenderer.TEXT) {
            oneAnswerInterval = 0.1;
//        } else if (taskRenderer == TaskRenderer.TEXT_ANIMATION) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_EQUATION) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_SVG) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_PNG) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_ANALOG_CLOCK) {
//            otherInterval = warManager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_DIGITAL_CLOCK) {
//            otherInterval = warManager.getDifficulty() * 500;
        } else {
            oneAnswerInterval = 0.2;
        }
        double sumInterval = hobbyImpact(oneAnswerInterval * manager.getContainer().getAnswerCount());
        return sumInterval * (4 - getWisie().getSpeedF1() - getWisie().getConcentrationF1() - getWisie().getIntuitionF1() - getWisie().getCunningF1());
    }


    @Override
    public void after() {
        if (thinkKnowAnswer) {
            manager.getFlow().run("NOW_KNOW_ANSWER");
        } else {
            manager.getFlow().run("DOESNT_KNOW_ANSWER");
        }
    }
}