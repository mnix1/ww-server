package com.ww.game.member.state.wisie.answer;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieAnswerCommand;
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
    private double attributePart;
    private double paramsPart;
    private double random;

    public MemberWisieAnsweredState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.ANSWERED);
    }

    protected Long prepareAnswerId() {
        if (correct) {
            return manager.getContainer().getQuestion().findCorrectAnswerId();
        }
        return randomElement(new ArrayList<>(manager.getContainer().getQuestion().getAnswers())).getId();
    }

    @Override
    public void initCommands() {
        super.initCommands();
        commands.add(new MemberWisieAnswerCommand(manager, answerId));
    }

    @Override
    public void initProps() {
        paramsPart =(double) params.get("paramsPart");
        attributePart = (paramsPart / 2 - 0.5) * 4 / 5;
        chanceCorrect = 0.5 + manager.getContainer().difficultyPart(0.1) + attributePart + getWisie().getHobbyPart();
        random = randomDouble();
        correct = chanceCorrect >= random;
        answerId = prepareAnswerId();
    }

    @Override
    public String toString() {
        return super.toString() + ", correct=" + correct + ", answerId=" + answerId + ", chanceCorrect=" + chanceCorrect + ", attributePart=" + attributePart + ", random=" + random;
    }
}
