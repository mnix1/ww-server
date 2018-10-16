package com.ww.manager.wisieanswer.state.phase5;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.entity.outside.rival.task.Answer;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateAnsweringPhase5 extends WisieState {
    private Boolean correctAnswer;
    private Double chance;
    private Long answerId;

    public WisieStateAnsweringPhase5(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", correctAnswer=" + correctAnswer+ ", answerId=" + answerId;
    }

    @Override
    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        double diffPart = (4 - manager.getDifficulty()) * 0.1;
        double attrPart = ((manager.getWarWisie().getWisdomSum() + 2 * manager.getWarWisie().getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        correctAnswer = chance > randomDouble();
        Answer answer = correctAnswer
                ? manager.getQuestion().getAnswers().stream().filter(Answer::getCorrect).findFirst().get()
                : randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        answerId = answer.getId();
        manager.getWarManager().getFlow().wisieAnswered(manager.getProfileId(), answerId);
    }
}
