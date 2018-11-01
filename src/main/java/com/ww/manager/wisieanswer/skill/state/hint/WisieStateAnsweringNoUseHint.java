package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.entity.outside.rival.task.Answer;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateAnsweringNoUseHint extends WisieSkillState {
    private Long hintAnswerId;

    private Boolean correctAnswer;
    private Double chance;

    public WisieStateAnsweringNoUseHint(WisieAnswerManager manager, Long hintAnswerId) {
        super(manager, STATE_TYPE_VOID);
        this.hintAnswerId = hintAnswerId;
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", correctAnswer=" + correctAnswer;
    }

    @Override
    protected void processVoid() {
        manager.addAndSendAction(MemberWisieStatus.ANSWERED);
        double diffPart = (4 - manager.getDifficulty()) * 0.1;
        double attrPart = ((manager.getWarWisie().getWisdomSum() + 2 * manager.getWarWisie().getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        correctAnswer = chance > randomDouble();
        Answer answer = correctAnswer
                ? manager.getQuestion().getAnswers().stream().filter(Answer::getCorrect).findFirst().get()
                : randomElement(new ArrayList<>(manager.getQuestion().getAnswers().stream().filter(a -> !a.getId().equals(hintAnswerId)).collect(Collectors.toList())));
        manager.getWarManager().getFlow().wisieAnswered(manager.getProfileId(), answer.getId());
    }
}
