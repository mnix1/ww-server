package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillThinkingIfUseHintState extends PlaySkillState {
    private Long hintAnswerId;
    private boolean hintCorrect;
    private double chanceNotUseIncorrectHint;
    private double attributePart;
    private boolean useHint;

    public PlaySkillThinkingIfUseHintState(PlaySkillFlow flow, MemberWisieManager manager, Long hintAnswerId) {
        super(flow, manager);
        this.hintAnswerId = hintAnswerId;
    }

    protected void init() {
        hintCorrect = getContainer().getQuestion().findCorrectAnswerId().equals(hintAnswerId);
        if (hintCorrect) {
            useHint = true;
            chanceNotUseIncorrectHint = 1;
        } else {
            attributePart = ((getWisie().getWisdomSum() + getWisie().getIntuitionF1() + getWisie().getConfidenceF1()) / 3 - 0.5) * 4 / 5;
            chanceNotUseIncorrectHint = 0.5 + getContainer().difficultyPart(0.05) + attributePart + getWisie().getHobbyPart();
            useHint = chanceNotUseIncorrectHint <= randomDouble();
        }
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.THINKING_IF_USE_HINT));
    }

    @Override
    protected double prepareInterval() {
        double interval = hobbyImpact(getContainer().getAnswerCount() * 0.25
                + 4 - getWisie().getWisdomSum() - getWisie().getConfidenceF1() - getWisie().getCunningF1() - getWisie().getIntuitionF1());
        if (hintCorrect) {
            return interval / 2;
        }
        return interval;
    }

    @Override
    public String toString() {
        return super.toString() + ", hintAnswerId=" + hintAnswerId + ", hintCorrect=" + hintCorrect
                + ", chanceNotUseIncorrectHint=" + chanceNotUseIncorrectHint
                + ", attributePart=" + attributePart + ", useHint=" + useHint;
    }

    @Override
    public void after() {
        if (useHint) {
            flow.run("WILL_USE_HINT");
        } else {
            flow.run("WONT_USE_HINT");
        }
    }
}
