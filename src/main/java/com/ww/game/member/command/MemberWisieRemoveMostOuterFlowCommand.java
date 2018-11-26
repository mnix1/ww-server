package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillFlow;

public class MemberWisieRemoveMostOuterFlowCommand extends GameCommand {
    private PlaySkillFlow flow;
    private Long profileId;

    public MemberWisieRemoveMostOuterFlowCommand(PlaySkillFlow flow, Long profileId) {
        this.flow = flow;
        this.profileId = profileId;
    }

    @Override
    public void execute() {
        PlayWarAnsweringFlowContainer flowContainer = flow.getFlowContainer();
        flowContainer.removeMostOuter(profileId);
    }
}
