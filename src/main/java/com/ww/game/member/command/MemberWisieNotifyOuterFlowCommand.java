package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;

public class MemberWisieNotifyOuterFlowCommand extends GameCommand {
    private GameFlow flow;
    private MemberWisieManager manager;

    public MemberWisieNotifyOuterFlowCommand(GameFlow flow, MemberWisieManager manager) {
        this.flow = flow;
        this.manager = manager;
    }

    @Override
    public void execute() {
        flow.notifyOuter(manager.getFlow(), false);
    }
}
