package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieAnswerCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.entity.outside.rival.task.Answer;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class PlaySkillAnsweringWithoutHintState extends PlaySkillState {
    private Long hintAnswerId;
    private double difficultyPart;
    private double attributePart;
    private double chanceCorrect;
    private boolean correctAnswer;
    private Long answerId;

    public PlaySkillAnsweringWithoutHintState(PlaySkillFlow flow, MemberWisieManager manager, Long hintAnswerId) {
        super(flow, manager);
        this.hintAnswerId = hintAnswerId;
    }

    private void init() {
        difficultyPart = (4 - getContainer().getDifficulty()) * 0.1;
        attributePart = ((getWisie().getWisdomSum() + 2 * getWisie().getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        chanceCorrect = 0.5 + difficultyPart + attributePart + getWisie().getHobbyPart();
        correctAnswer = chanceCorrect > randomDouble();
        answerId = findAnswerId();
    }

    private Long findAnswerId() {
        if (correctAnswer) {
            return manager.getContainer().getQuestion().findCorrectAnswerId();
        }
        return findRandomAnswerId();
    }

    private Long findRandomAnswerId() {
        List<Long> answerIds = manager.getContainer().getQuestion().getAnswers().stream()
                .filter(answer -> !answer.getId().equals(hintAnswerId))
                .map(Answer::getId)
                .collect(Collectors.toList());
        return randomElement(answerIds);
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.ANSWERED));
        commands.add(new MemberWisieAnswerCommand(manager, answerId));
    }

    @Override
    public void after() {
    }
}
