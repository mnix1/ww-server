package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.entity.outside.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateAnsweringPhase3 extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnsweringPhase3.class);

    public StateAnsweringPhase3(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        double diffPart = (4 - manager.getDifficulty()) * 0.1;
        double attrPart = ((2 * manager.getWisdomSum() + manager.getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean correctAnswer = chance > randomDouble();
        logger.trace(manager.toString() + ", chance: " + chance + ", correctAnswer: " + correctAnswer);
        Answer answer = correctAnswer
                ? manager.getQuestion().getAnswers().stream().filter(Answer::getCorrect).findFirst().get()
                : randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), answer.getId());
    }
}
