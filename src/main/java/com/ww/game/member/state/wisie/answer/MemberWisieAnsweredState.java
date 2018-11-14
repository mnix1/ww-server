package com.ww.game.member.state.wisie.answer;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class MemberWisieAnsweredState extends MemberWisieState {
    private Long answerId;
    private boolean correct;
    private double chanceCorrect;
    private double difficultyPart;
    private double attributePart;
    private double paramsPart;

    public MemberWisieAnsweredState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.ANSWERED);
    }

    protected Long prepareAnswerId() {
        if (correct) {
            return manager.getContainer().getQuestion().findCorrectAnswerId();
        }
        return randomElement(new ArrayList<>(manager.getContainer().getQuestion().getAnswers())).getId();
    }

    public void answer() {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId);
        manager.getPlayManager().processMessage(manager.getContainer().getTeam().getProfileId(), content);
    }

    private void init() {
        difficultyPart = (4 - manager.getContainer().getDifficulty()) * 0.1;
        paramsPart = params.containsKey("paramsPart") ? (double) params.get("paramsPart") : 2 * getWisie().getWisdomSum() + 2 * getWisie().getIntuitionF1();
        attributePart = (paramsPart / 2 - 0.5) * 4 / 5;
        chanceCorrect = 0.5 + difficultyPart + attributePart + getWisie().getHobbyPart();
        correct = chanceCorrect >= randomDouble();
        answerId = prepareAnswerId();
    }

    @Override
    public String toString() {
        return super.toString() + ", correct=" + correct + ", answerId=" + answerId + ", chanceCorrect=" + chanceCorrect + ", difficultyPart=" + difficultyPart + ", attributePart=" + attributePart;
    }

    @Override
    public void execute() {
        init();
        super.execute();
        answer();
    }
}
