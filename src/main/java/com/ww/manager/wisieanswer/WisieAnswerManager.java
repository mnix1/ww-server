package com.ww.manager.wisieanswer;

import com.ww.helper.AnswerHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.state.hint.*;
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
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.helper.WisieHelper.f1;

@Getter
@Setter
public class WisieAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerManager.class);

    private CopyOnWriteArrayList<WisieAnswerAction> actions = new CopyOnWriteArrayList<>();

    private boolean inProgress = false;

    private OwnedWisie wisie;
    private WarManager manager;
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

    private boolean receivedHint = false;
    private Long hintAnswerId;
    private boolean hintCorrect;

    private boolean waterPistolUsedOnIt = false;

    private WisieAnswerFlow flow;

    public WisieAnswerManager(OwnedWisie wisie, WarManager manager) {
        this.wisie = wisie;
        this.manager = manager;
        this.question = manager.getContainer().getQuestions().get(manager.getContainer().getCurrentTaskIndex());
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.isHobby = wisie.getHobbies().contains(question.getType().getCategory());
        this.hobbyCount = wisie.getHobbies().size();
        this.hobbyFactor = 1 + 1d / hobbyCount;
        this.cacheAttributes();
        logger.trace(toString() +
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
        this.flow = new WisieAnswerFlow(this);
    }

    @Override
    public String toString() {
        return getWisie().getId() + ", profileId=" + getWisie().getProfile().getId() + ", wisieName=" + getWisie().getWisie().getNamePolish() + ", lastAction=" + lastAction().name();
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

    public WisieAnswerAction lastAction() {
        if (actions.isEmpty()) {
            return WisieAnswerAction.NONE;
        }
        return actions.get(actions.size() - 1);
    }

    public void addAndSendAction(WisieAnswerAction action) {
        actions.add(action);
        manager.getContainer().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelWisieAnswering(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
    }

}
