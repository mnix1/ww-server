package com.ww.manager.wisieanswer;

import com.ww.helper.AnswerHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.state.phase3.StateAnsweringPhase3;
import com.ww.manager.wisieanswer.state.multiphase.StateCheckNoConcentration;
import com.ww.manager.wisieanswer.state.multiphase.StateLostConcentration;
import com.ww.manager.wisieanswer.state.phase1.StateStartRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateCheckKnowAnswerAfterThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateEndRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase3.*;
import com.ww.manager.wisieanswer.state.phase4.StateEndRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase4.StateStartRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase5.StateAnsweringPhase5;
import com.ww.manager.wisieanswer.state.phase5.StateCheckKnowAnswerAfterThinkingWhichMatch;
import com.ww.manager.wisieanswer.state.phase5.StateEndThinkingWhichAnswerMatch;
import com.ww.manager.wisieanswer.state.phase5.StateThinkKnowAnswer;
import com.ww.manager.wisieanswer.state.phase6.*;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.entity.wisie.OwnedWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.helper.WisieHelper.f1;

@Getter
public class WisieAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    private boolean inProgress = false;

    private CopyOnWriteArrayList<WisieAnswerAction> actions = new CopyOnWriteArrayList<>();

    private OwnedWisie wisie;
    private WarManager warManager;
    private WarContainer warContainer;
    private Question question;

    private int difficulty;
    private int answerCount;

    private double wisdomSum;
    private double speedF1;
    private double reflexF1;
    private double concentrationF1;
    private double confidenceF1;
    private double intuitionF1;

    private boolean isHobby;
    private int hobbyCount;
    private double hobbyFactor;

    public WisieAnswerManager(OwnedWisie wisie, RivalManager rivalManager) {
        this.wisie = wisie;
        this.warManager = (WarManager) rivalManager;
        this.warContainer = (WarContainer) this.warManager.getRivalContainer();
        this.question = warContainer.getQuestions().get(warContainer.getCurrentTaskIndex());
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.isHobby = wisie.getHobbies().contains(question.getType().getCategory());
        this.hobbyCount = wisie.getHobbies().size();
        this.hobbyFactor = 1 + 1d / hobbyCount;
        this.cacheAttributes();
        logger.trace(getWisie().getWisie().getNamePolish() +
                ", difficulty=" + difficulty +
                ", answerCount=" + answerCount +
                ", wisdomSum=" + wisdomSum +
                ", speedF1=" + speedF1 +
                ", reflexF1=" + reflexF1 +
                ", concentrationF1=" + concentrationF1 +
                ", confidenceF1=" + confidenceF1 +
                ", intuitionF1=" + intuitionF1 +
                ", isHobby=" + isHobby +
                ", hobbyCount=" + hobbyCount +
                ", hobbyFactor=" + hobbyFactor);
    }

    private void cacheAttributes() {
        this.wisdomSum = prepareWisdomSum();
        this.speedF1 = f1(wisie.getMentalAttributeSpeed());
        this.reflexF1 = f1(wisie.getMentalAttributeReflex());
        this.concentrationF1 = f1(wisie.getMentalAttributeConcentration());
        this.confidenceF1 = f1(wisie.getMentalAttributeConfidence());
        this.intuitionF1 = f1(wisie.getMentalAttributeIntuition());
    }

    private double prepareWisdomSum() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(wisie.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

    public void start() {
        inProgress = true;
        phase1();
    }

    public void stop() {
        if (inProgress) {
            inProgress = false;
        }
    }

    public void phase1() {
        new StateStartRecognizingQuestion(this).startFlowable().subscribe(aLong1 -> {
            WisieAnswerAction aa1 = new StateCheckNoConcentration(this).startWisieAnswerAction();
            if (WisieAnswerAction.isNoConcentration(aa1)) {
                new StateLostConcentration(this, aa1).startFlowable().subscribe(aLong2 -> {
                    phase2();
                });
            } else {
                phase2();
            }
        });

    }

    public void phase2() {
        new StateEndRecognizingQuestion(this).startFlowable().subscribe(aLong2 -> {
            new StateStartThinkingAboutQuestion(this).startFlowable().subscribe(aLong3 -> {
                WisieAnswerAction aa2 = new StateCheckKnowAnswerAfterThinkingAboutQuestion(this).startWisieAnswerAction();
                if (aa2 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                    phase3();
                } else if (aa2 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                    phase4();
                }
            });
        });
    }

    public void phase3() {
        new StateStartLookingForAnswer(this).startFlowable().subscribe(aLong4 -> {
            new StateEndLookingForAnswer(this).startFlowable().subscribe(aLong5 -> {
                WisieAnswerAction aa2 = new StateCheckFoundAnswerLookingFor(this).startWisieAnswerAction();
                if (aa2 == WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR) {
                    new StateFoundAnswerLookingFor(this).startFlowable().subscribe(aLong6 -> {
                        new StateAnsweringPhase3(this).startVoid();
                    });
                } else if (aa2 == WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR) {
                    new StateNoFoundAnswerLookingFor(this).startFlowable().subscribe(aLong7 -> {
                        phase5();
                    });
                }
            });
        });
    }

    public void phase4() {
        new StateStartRecognizingAnswers(this).startFlowable().subscribe(aLong5 -> {
            new StateEndRecognizingAnswers(this).startFlowable().subscribe(aLong6 -> {
                WisieAnswerAction aa3 = new StateCheckNoConcentration(this).startWisieAnswerAction();
                if (WisieAnswerAction.isNoConcentration(aa3)) {
                    new StateLostConcentration(this, aa3).startFlowable().subscribe(aLong7 -> {
                        phase5();
                    });
                } else {
                    phase5();
                }
            });
        });
    }

    public void phase5() {
        new StateEndThinkingWhichAnswerMatch(this).startFlowable().subscribe(aLong8 -> {
            WisieAnswerAction aa4 = new StateCheckKnowAnswerAfterThinkingWhichMatch(this).startWisieAnswerAction();
            if (aa4 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                new StateThinkKnowAnswer(this).startFlowable().subscribe(aLong9 -> {
                    new StateAnsweringPhase5(this).startVoid();
                });
            } else if (aa4 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                phase6();
            }
        });
    }

    public void phase6() {
        new StateNotSureOfAnswer(this).startFlowable().subscribe(aLong10 -> {
            new StateEndThinkingIfGiveRandomAnswer(this).startFlowable().subscribe(aLong11 -> {
                WisieAnswerAction aa5 = new StateCheckIfGiveRandomAnswer(this).startWisieAnswerAction();
                if (aa5 == WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER) {
                    new StateAnsweringPhase6(this).startVoid();
                } else if (aa5 == WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER) {
                    new StateSurrender(this).startVoid();
                }
            });
        });
    }

    public WisieAnswerAction lastAction() {
        return actions.get(actions.size() - 1);
    }

    public void addAndSendAction(WisieAnswerAction action) {
        actions.add(action);
        warContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            warContainer.fillModelWisieAnswering(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });

    }

}
