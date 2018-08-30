package com.ww.manager.heroanswer.state.phase2;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.constant.rival.task.TaskRenderer;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndRecognizingQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndRecognizingQuestion.class);
    private static final long ONE_CHAR_READING_SPEED = 40;//ms

    public StateEndRecognizingQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.RECOGNIZING_QUESTION);
        long readingInterval = manager.getQuestion().getTextContentEnglish().length() * ONE_CHAR_READING_SPEED;
        long otherInterval;
        TaskRenderer taskRenderer = manager.getQuestion().getType().getQuestionRenderer();
        if (taskRenderer == TaskRenderer.TEXT) {
            otherInterval = 0;
        } else if (taskRenderer == TaskRenderer.TEXT_ANIMATION) {
            otherInterval = manager.getDifficulty() * 1000;
        } else if (taskRenderer == TaskRenderer.TEXT_EQUATION) {
            otherInterval = manager.getDifficulty() * 1000;
        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_SVG) {
            otherInterval = manager.getDifficulty() * 1000;
        } else if (taskRenderer == TaskRenderer.TEXT_IMAGE_PNG) {
            otherInterval = manager.getDifficulty() * 1000;
        } else if (taskRenderer == TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG) {
            otherInterval = manager.getDifficulty() * 1000;
        } else if (taskRenderer == TaskRenderer.TEXT_DATE) {
            otherInterval = manager.getDifficulty() * 1000;
        } else {
            otherInterval = manager.getDifficulty() * 1000;
        }
        double sumInterval = readingInterval + randomDouble(otherInterval, otherInterval * 2);
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * (4 - 2 * manager.getSpeedF1() - manager.getConcentrationF1()));
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
