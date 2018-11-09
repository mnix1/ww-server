package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieAddDisguiseCommand extends GameCommand {
    private MemberWisieManager manager;
    private DisguiseType disguiseType;

    public MemberWisieAddDisguiseCommand(MemberWisieManager manager, DisguiseType disguiseType){
        this.manager = manager;
        this.disguiseType = disguiseType;
    }

    @Override
    public void execute() {
        manager.getContainer().getMember().addDisguise(disguiseType);
    }
}
