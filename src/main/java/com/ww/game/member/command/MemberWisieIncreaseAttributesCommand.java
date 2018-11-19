package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import lombok.ToString;

@ToString
public class MemberWisieIncreaseAttributesCommand extends GameCommand {
    private MemberWisieManager manager;

    public MemberWisieIncreaseAttributesCommand(MemberWisieManager manager){
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.getContainer().getMember().increaseAttributesByHalf();
        manager.getContainer().getMember().refreshCache(manager.getContainer().getQuestion());
    }
}
