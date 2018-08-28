package com.ww.manager.heroanswer;

import com.ww.manager.RivalManager;
import com.ww.manager.WarManager;
import com.ww.manager.heroanswer.state.*;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HeroAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    private boolean inProgress = false;

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

    public void start() {
        inProgress = true;
        new StateStartReading(this).startFlowable().subscribe(aLong1 -> {
            new StateEndReading(this).startFlowable().subscribe(aLong2 -> {
                new StateStartThinking(this).startFlowable().subscribe(aLong3 -> {
                    HeroAnswerAction answerAction = new StateEndThinking(this).startHeroAnswerAction();
                    if (answerAction == HeroAnswerAction.THINK_KNOW_ANSWER) {
                        new StateStartSearchingAnswer(this).startFlowable().subscribe(aLong4 -> {
                            new StateEndSearchingAnswer(this).startFlowable().subscribe(aLong5 -> {
                                new StateAnswering(this).startVoid();
                            });
                        });
                    }
                });
            });
        });
    }

    public void stop() {
        if (inProgress) {
            inProgress = false;
        }
    }

    public HeroAnswerAction lastAction() {
        return actions.get(actions.size() - 1);
    }

    public void addAndSendAction(HeroAnswerAction action) {
//        logger.error(hero.getHero().getNamePolish() + " " + action.name());
        actions.add(action);
        warContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            warContainer.fillModelHeroAnswering(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });

    }

    public Double f1(Double arg) {
        double log = Math.log(arg + 1);
        return log / (6 + log);
    }

    public Double f2(Double arg) {
        double log = Math.log(arg + 1);
        return log * 0.75 / (6 + log) + 0.25;
    }

    public double sumWisdomAttributeF1() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(hero.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

    public double sumWisdomAttributeF2() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f2(hero.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

}
