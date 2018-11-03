package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieRecognizingQuestionState extends MemberWisieIntervalState {
    private static final double ONE_CHAR_READING_SPEED = 0.040;//40ms

    public MemberWisieRecognizingQuestionState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.RECOGNIZING_QUESTION);
    }

    @Override
    protected double prepareInterval() {
        double readingInterval = container.getQuestion().getTextContent().length() * ONE_CHAR_READING_SPEED;
        double otherInterval;
        TaskRenderer taskRenderer = container.getQuestion().getType().getQuestionRenderer();
        if (taskRenderer == TaskRenderer.TEXT) {
            otherInterval = 0;
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
            otherInterval = container.getDifficulty() * 0.5;
        }
        double sumInterval = hobbyImpact(readingInterval + randomDouble(otherInterval / 0.5, otherInterval));
        return sumInterval * (1 - getWisie().getSpeedF1());
    }
}