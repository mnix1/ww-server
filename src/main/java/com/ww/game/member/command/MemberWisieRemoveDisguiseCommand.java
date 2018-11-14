package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.DisguiseType;
import lombok.ToString;

@ToString
public class MemberWisieRemoveDisguiseCommand extends GameCommand {
    private MemberWisieManager manager;

    public MemberWisieRemoveDisguiseCommand(MemberWisieManager manager){
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.getContainer().getMember().removeDisguise();
    }
}
