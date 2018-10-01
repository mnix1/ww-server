package com.ww.manager.wisieanswer.state.phase2;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.constant.rival.task.TaskRenderer;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndRecognizingQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndRecognizingQuestion.class);
    private static final long ONE_CHAR_READING_SPEED = 40;//ms

    public StateEndRecognizingQuestion(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.RECOGNIZING_QUESTION);
        long readingInterval = manager.getQuestion().getTextContent().length() * ONE_CHAR_READING_SPEED;
        long otherInterval;
        TaskRenderer taskRenderer = manager.getQuestion().getType().getQuestionRenderer();
        if (taskRenderer == TaskRenderer.TEXT) {
            otherInterval = 0;
//        } else if (taskRenderer == TaskRenderer.TEXT_ANIMATION) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_EQUATION) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_SVG) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_PNG) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_ANALOG_CLOCK) {
//            otherInterval = manager.getDifficulty() * 500;
//        } else if (taskRenderer == TaskRenderer.TEXT_DIGITAL_CLOCK) {
//            otherInterval = manager.getDifficulty() * 500;
        } else {
            otherInterval = manager.getDifficulty() * 500;
        }
        double sumInterval = readingInterval + randomDouble(otherInterval / 0.5, otherInterval);
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * (3 - 2 * manager.getSpeedF1() - manager.getConcentrationF1()));
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
