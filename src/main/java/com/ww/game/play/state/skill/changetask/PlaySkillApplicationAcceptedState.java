package com.ww.game.play.state.skill.changetask;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillApplicationAcceptedState extends PlaySkillState {

    public PlaySkillApplicationAcceptedState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.SUBMITS_APPLICATION));
    }
}
