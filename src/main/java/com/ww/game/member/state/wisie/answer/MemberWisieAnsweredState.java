package com.ww.game.member.state.wisie.answer;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.member.state.wisie.interval.MemberWisieIntervalState;
import com.ww.game.play.PlayManager;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.entity.outside.rival.task.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomElement;

public class MemberWisieAnsweredState extends MemberWisieState {
    protected PlayManager playManager;

    public MemberWisieAnsweredState(MemberWisieContainer container, PlayManager playManager) {
        super(container, MemberWisieStatus.ANSWERED);
        this.playManager = playManager;
    }

    protected boolean isCorrect() {
        return true;
    }

    protected Long prepareAnswerId() {
        if (isCorrect()) {
            return container.getQuestion().findCorrectAnswerId();
        }
        return randomElement(new ArrayList<>(container.getQuestion().getAnswers())).getId();
    }

    public void answer() {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", prepareAnswerId());
        playManager.getCommunication().processMessage(container.getTeam().getProfileId(), content);
    }

}
