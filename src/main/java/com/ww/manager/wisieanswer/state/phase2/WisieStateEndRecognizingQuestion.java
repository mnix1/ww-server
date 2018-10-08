package com.ww.manager.wisieanswer.state.phase2;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.constant.rival.task.TaskRenderer;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndRecognizingQuestion extends WisieState {
    private static final long ONE_CHAR_READING_SPEED = 40;//ms

    private Long interval;

    public WisieStateEndRecognizingQuestion(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.RECOGNIZING_QUESTION);
        long readingInterval = manager.getQuestion().getTextContent().length() * ONE_CHAR_READING_SPEED;
        long otherInterval;
        TaskRenderer taskRenderer = manager.getQuestion().getType().getQuestionRenderer();
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
            otherInterval = manager.getDifficulty() * intervalMultiply() / 2;
        }
        double sumInterval = readingInterval + randomDouble(otherInterval / 0.5, otherInterval);
        if (manager.getWarWisie().isHobby()) {
            sumInterval /= manager.getWarWisie().getHobbyFactor();
        }
        interval = (long) (sumInterval * (3 - 2 * manager.getWarWisie().getSpeedF1() - manager.getWarWisie().getConcentrationF1()));
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
