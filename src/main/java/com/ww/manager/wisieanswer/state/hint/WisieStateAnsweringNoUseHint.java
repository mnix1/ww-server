package com.ww.manager.wisieanswer.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.entity.outside.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateAnsweringNoUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateAnsweringNoUseHint.class);

    public WisieStateAnsweringNoUseHint(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        double diffPart = (4 - manager.getDifficulty()) * 0.1;
        double attrPart = ((manager.getWisdomSum() + 2 * manager.getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean correctAnswer = chance > randomDouble();
        logger.trace(manager.toString() + ", chance: " + chance + ", correctAnswer: " + correctAnswer);
        Answer answer = correctAnswer
                ? manager.getQuestion().getAnswers().stream().filter(Answer::getCorrect).findFirst().get()
                : randomElement(new ArrayList<>(manager.getQuestion().getAnswers().stream().filter(a -> !a.getId().equals(manager.getHintAnswerId())).collect(Collectors.toList())));
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), answer.getId());
    }
}
