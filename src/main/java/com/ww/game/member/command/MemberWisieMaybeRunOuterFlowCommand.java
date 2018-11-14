package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import lombok.ToString;

@ToString
public class MemberWisieMaybeRunOuterFlowCommand extends GameCommand {
    private MemberWisieManager manager;

    public MemberWisieMaybeRunOuterFlowCommand(MemberWisieManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.getFlow().cancelInnerMaybeStartAfter();
    }
}
