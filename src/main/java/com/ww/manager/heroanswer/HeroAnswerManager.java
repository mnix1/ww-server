package com.ww.manager.heroanswer;

import com.ww.helper.AnswerHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.heroanswer.state.phase3.StateAnsweringPhase3;
import com.ww.manager.heroanswer.state.multiphase.StateCheckNoConcentration;
import com.ww.manager.heroanswer.state.multiphase.StateLostConcentration;
import com.ww.manager.heroanswer.state.phase1.StateStartRecognizingQuestion;
import com.ww.manager.heroanswer.state.phase2.StateCheckKnowAnswerAfterThinkingAboutQuestion;
import com.ww.manager.heroanswer.state.phase2.StateEndRecognizingQuestion;
import com.ww.manager.heroanswer.state.phase2.StateStartThinkingAboutQuestion;
import com.ww.manager.heroanswer.state.phase3.*;
import com.ww.manager.heroanswer.state.phase4.StateEndRecognizingAnswers;
import com.ww.manager.heroanswer.state.phase4.StateStartRecognizingAnswers;
import com.ww.manager.heroanswer.state.phase5.StateAnsweringPhase5;
import com.ww.manager.heroanswer.state.phase5.StateCheckKnowAnswerAfterThinkingWhichMatch;
import com.ww.manager.heroanswer.state.phase5.StateEndThinkingWhichAnswerMatch;
import com.ww.manager.heroanswer.state.phase5.StateThinkKnowAnswer;
import com.ww.manager.heroanswer.state.phase6.*;
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

import static com.ww.helper.HeroHelper.f1;

@Getter
public class HeroAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    private boolean inProgress = false;


    private List<HeroAnswerAction> actions = new ArrayList<>();

    private ProfileHero hero;
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

    public HeroAnswerManager(ProfileHero hero, RivalManager rivalManager) {
        this.hero = hero;
        this.warManager = (WarManager) rivalManager;
        this.warContainer = (WarContainer) this.warManager.rivalContainer;
        this.question = warContainer.getQuestions().get(warContainer.getCurrentTaskIndex());
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.isHobby = hero.getHobbies().contains(question.getType().getCategory());
        this.hobbyCount = hero.getHobbies().size();
        this.hobbyFactor = 1 + 1d / hobbyCount;
        this.cacheAttributes();
        logger.debug(getHero().getHero().getNamePolish() +
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
        this.speedF1 = f1(hero.getMentalAttributeSpeed());
        this.reflexF1 = f1(hero.getMentalAttributeReflex());
        this.concentrationF1 = f1(hero.getMentalAttributeConcentration());
        this.confidenceF1 = f1(hero.getMentalAttributeConfidence());
        this.intuitionF1 = f1(hero.getMentalAttributeIntuition());
    }

    private double prepareWisdomSum() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(hero.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
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
            HeroAnswerAction aa1 = new StateCheckNoConcentration(this).startHeroAnswerAction();
            if (HeroAnswerAction.isNoConcentration(aa1)) {
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
                HeroAnswerAction aa2 = new StateCheckKnowAnswerAfterThinkingAboutQuestion(this).startHeroAnswerAction();
                if (aa2 == HeroAnswerAction.THINK_KNOW_ANSWER) {
                    phase3();
                } else if (aa2 == HeroAnswerAction.NOT_SURE_OF_ANSWER) {
                    phase4();
                }
            });
        });
    }

    public void phase3() {
        new StateStartLookingForAnswer(this).startFlowable().subscribe(aLong4 -> {
            new StateEndLookingForAnswer(this).startFlowable().subscribe(aLong5 -> {
                HeroAnswerAction aa2 = new StateCheckFoundAnswerLookingFor(this).startHeroAnswerAction();
                if (aa2 == HeroAnswerAction.FOUND_ANSWER_LOOKING_FOR) {
                    new StateFoundAnswerLookingFor(this).startFlowable().subscribe(aLong6 -> {
                        new StateAnsweringPhase3(this).startVoid();
                    });
                } else if (aa2 == HeroAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR) {
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
                HeroAnswerAction aa3 = new StateCheckNoConcentration(this).startHeroAnswerAction();
                if (HeroAnswerAction.isNoConcentration(aa3)) {
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
            HeroAnswerAction aa4 = new StateCheckKnowAnswerAfterThinkingWhichMatch(this).startHeroAnswerAction();
            if (aa4 == HeroAnswerAction.THINK_KNOW_ANSWER) {
                new StateThinkKnowAnswer(this).startFlowable().subscribe(aLong9 -> {
                    new StateAnsweringPhase5(this).startVoid();
                });
            } else if (aa4 == HeroAnswerAction.NOT_SURE_OF_ANSWER) {
                phase6();
            }
        });
    }

    public void phase6() {
        new StateNotSureOfAnswer(this).startFlowable().subscribe(aLong10 -> {
            new StateEndThinkingIfGiveRandomAnswer(this).startFlowable().subscribe(aLong11 -> {
                HeroAnswerAction aa5 = new StateCheckIfGiveRandomAnswer(this).startHeroAnswerAction();
                if (aa5 == HeroAnswerAction.WILL_GIVE_RANDOM_ANSWER) {
                    new StateAnsweringPhase6(this).startVoid();
                } else if (aa5 == HeroAnswerAction.WONT_GIVE_RANDOM_ANSWER) {
                    new StateSurrender(this).startVoid();
                }
            });
        });
    }

    public HeroAnswerAction lastAction() {
        return actions.get(actions.size() - 1);
    }

    public void addAndSendAction(HeroAnswerAction action) {
        actions.add(action);
        warContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            warContainer.fillModelHeroAnswering(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });

    }

}
