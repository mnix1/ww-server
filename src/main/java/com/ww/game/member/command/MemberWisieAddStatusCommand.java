package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.ToString;

@ToString
public class MemberWisieAddStatusCommand extends GameCommand {
    private MemberWisieManager manager;
    private MemberWisieStatus status;

    public MemberWisieAddStatusCommand(MemberWisieManager manager, MemberWisieStatus status){
        this.manager = manager;
        this.status = status;
    }

    @Override
    public void execute() {
        manager.getContainer().addAction(status);
    }
}
