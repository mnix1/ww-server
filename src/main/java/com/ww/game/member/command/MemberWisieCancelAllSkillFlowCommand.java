package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import lombok.ToString;

@ToString
public class MemberWisieCancelAllSkillFlowCommand extends GameCommand {
    private PlaySkillFlow flow;
    private Long profileId;

    public MemberWisieCancelAllSkillFlowCommand(PlaySkillFlow flow, Long profileId) {
        this.flow = flow;
        this.profileId = profileId;
    }

    @Override
    public void execute() {
        PlayWarAnsweringFlowContainer flowContainer = flow.getFlowContainer();
        flowContainer.removeAllOutersAndRun(profileId);
    }
}
