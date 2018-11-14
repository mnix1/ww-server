package com.ww.game.member.state.wisie.answer;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.play.PlayManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomElement;

public class MemberWisieAnsweredState extends MemberWisieState {
    protected PlayManager playManager;

    public MemberWisieAnsweredState(MemberWisieManager manager, PlayManager playManager) {
        super(manager, MemberWisieStatus.ANSWERED);
        this.playManager = playManager;
    }

    protected boolean isCorrect() {
        return true;
    }

    protected Long prepareAnswerId() {
        if (isCorrect()) {
            return manager.getContainer().getQuestion().findCorrectAnswerId();
        }
        return randomElement(new ArrayList<>(manager.getContainer().getQuestion().getAnswers())).getId();
    }

    public void answer() {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", prepareAnswerId());
        playManager.processMessage(manager.getContainer().getTeam().getProfileId(), content);
    }

}
