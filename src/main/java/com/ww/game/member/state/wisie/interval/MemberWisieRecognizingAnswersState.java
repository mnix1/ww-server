package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieRecognizingAnswersState extends MemberWisieIntervalState {
    public MemberWisieRecognizingAnswersState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.RECOGNIZING_ANSWERS);
    }

    @Override
    protected double prepareInterval() {
        double oneAnswerInterval;
        TaskRenderer taskRenderer = manager.getContainer().getQuestion().getType().getAnswerRenderer();
        if (taskRenderer == TaskRenderer.TEXT) {
            oneAnswerInterval = 0.2;
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
            oneAnswerInterval = 0.4;
        }
        double sumInterval = hobbyImpact(oneAnswerInterval * manager.getContainer().getAnswerCount());
        return sumInterval * (4 - getWisie().getSpeedF1() - getWisie().getConcentrationF1() - getWisie().getIntuitionF1() - getWisie().getCunningF1());
    }

    @Override
    public void after() {
        manager.getFlow().run("THINKING_WHICH_ANSWER_MATCH");
    }
}