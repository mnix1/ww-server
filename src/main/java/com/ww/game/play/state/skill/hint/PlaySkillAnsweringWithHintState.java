package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieAnswerCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillAnsweringWithHintState extends PlaySkillState {
    private Long answerId;

    public PlaySkillAnsweringWithHintState(PlaySkillFlow flow, Long answerId) {
        super(flow);
        this.answerId = answerId;
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
