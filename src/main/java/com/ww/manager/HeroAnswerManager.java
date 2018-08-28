package com.ww.manager;

import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import io.reactivex.Flowable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

@Getter
public class HeroAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    private boolean inProgress = false;

    private static final double MIN_START_READING_INTERVAL = 0.5;
    private static final double MIN_END_READING_INTERVAL = 2;
    private static final double MIN_THINKING_INTERVAL = 1;
    private static final double MIN_START_SEARCHING_ANSWER_INTERVAL = 0.5;

    private double getMinEndSearchingAnswerInterval() {
        return question.getAnswers().size() * 0.5;
    }

    private List<HeroAnswerAction> actions = new ArrayList<>();

    private ProfileHero hero;
    private WarManager warManager;
    private WarContainer warContainer;
    private Question question;

    public HeroAnswerManager(ProfileHero hero, RivalManager rivalManager) {
        this.hero = hero;
        this.warManager = (WarManager) rivalManager;
        this.warContainer = (WarContainer) this.warManager.rivalContainer;
        this.question = warContainer.getQuestions().get(warContainer.getCurrentTaskIndex());
    }

    private Double f1(Double arg) {
        double log = Math.log(arg + 1);
        return log / (6 + log);
    }

    private Double f2(Double arg) {
        double log = Math.log(arg + 1);
        return log * 0.75 / (6 + log) + 0.25;
    }

    public void start() {
        inProgress = true;
        stateStartReading();
    }

    public void stop() {
        if (inProgress) {
            inProgress = false;
        }
    }

    private void addAndSendAction(HeroAnswerAction action) {
        logger.error(hero.getHero().getNamePolish() + " " +  action.name());
        actions.add(action);
        warContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            warContainer.fillModelHeroAnswering(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });

    }

    private void stateStartReading() {
        if (!inProgress) {
            return;
        }
        addAndSendAction(HeroAnswerAction.WAITING_FOR_QUESTION);
        //[0.5;2.5]max
        //[0.5;0.5]min
        double reflexF1 = f1(hero.getMentalAttributeReflex());
        long interval = (long) randomDouble(MIN_START_READING_INTERVAL, (5 - 4 * reflexF1) * MIN_START_READING_INTERVAL) * 1000;
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    stateEndReading();
                });
    }

    private void stateEndReading() {
        if (!inProgress) {
            return;
        }
        addAndSendAction(HeroAnswerAction.READING_QUESTION);
        //[2;10]max
        //[2;2]min
        double speedF1 = f1(hero.getMentalAttributeSpeed());
        long interval = (long) randomDouble(MIN_END_READING_INTERVAL, (5 - 4 * speedF1) * MIN_END_READING_INTERVAL) * 1000;
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    stateStartThinking();
                });
    }

    private double sumWisdomAttributeF1() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(hero.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

    private void stateStartThinking() {
        if (!inProgress) {
            return;
        }
        addAndSendAction(HeroAnswerAction.THINKING);
        //[1;10]max
        //[1;1]min
        double sumWisdomAttributeF1 = sumWisdomAttributeF1();
        long interval = (long) randomDouble(MIN_THINKING_INTERVAL, (10 - 9 * sumWisdomAttributeF1) * MIN_THINKING_INTERVAL) * 1000;
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    stateEndThinking();
                });
    }

    private double sumWisdomAttributeF2() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f2(hero.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

    private void stateEndThinking() {
        if (!inProgress) {
            return;
        }
//        addAndSendAction(HeroAnswerAction.THINKING);
        double sumWisdomAttributeF2 = sumWisdomAttributeF2();
        boolean thinkKnowAnswer = sumWisdomAttributeF2 > randomDouble();
        if (thinkKnowAnswer) {
            addAndSendAction(HeroAnswerAction.THINK_KNOW_ANSWER);
            stateStartSearchingAnswer();
            return;
//            boolean knowAnswer = sumWisdomAttributeF2 > randomDouble();
//            if (knowAnswer) {
//
//            }
        }
    }

    private void stateStartSearchingAnswer() {
        if (!inProgress) {
            return;
        }
        //[0.5;2.5]max
        //[0.5;0.5]min
        double reflexF1 = f1(hero.getMentalAttributeReflex());
        double concentrationF1 = f1(hero.getMentalAttributeConcentration());
        long interval = (long) randomDouble(MIN_START_SEARCHING_ANSWER_INTERVAL, (5 - 2 * reflexF1 - 2 * concentrationF1) * MIN_START_SEARCHING_ANSWER_INTERVAL) * 1000;
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    stateEndSearchingAnswer();
                });
    }

    private void stateEndSearchingAnswer() {
        if (!inProgress) {
            return;
        }
        addAndSendAction(HeroAnswerAction.SEARCHING_FOR_ANSWER);
        double speedF1 = f1(hero.getMentalAttributeSpeed());
        double concentrationF1 = f1(hero.getMentalAttributeConcentration());
        double minInterval = getMinEndSearchingAnswerInterval();
        long interval = (long) randomDouble(minInterval, (5 - 2 * speedF1 - 2 * concentrationF1) * minInterval) * 1000;
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    stateAnswered();
                });
    }

    private void stateAnswered() {
        if (!inProgress) {
            return;
        }
        addAndSendAction(HeroAnswerAction.ANSWERED);
        double sumWisdomAttributeF2 = sumWisdomAttributeF2();
        boolean correctAnswer = sumWisdomAttributeF2 > randomDouble();
        Answer answer = question.getAnswers().stream().filter(a -> a.getCorrect() == correctAnswer).findFirst().get();
        warManager.heroAnswered(hero.getProfile().getId(), answer.getId());
    }
}
