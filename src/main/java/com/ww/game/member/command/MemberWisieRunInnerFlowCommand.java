package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.ToString;

@ToString
public class MemberWisieRunInnerFlowCommand extends GameCommand {
    private MemberWisieManager manager;
    private GameFlow flow;

    public MemberWisieRunInnerFlowCommand(MemberWisieManager manager, GameFlow flow) {
        this.manager = manager;
        this.flow = flow;
    }

    @Override
    public void execute() {
        manager.getFlow().innerFlow(flow);
    }
}
