package com.ww.game.play.command.war;

import com.ww.game.GameFlow;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Collection;

public class PlayWarStartMemberManagerCommand extends PlayCommand {
    private Collection<MemberWisieFlow> flows;

    public PlayWarStartMemberManagerCommand(PlayContainer container, Collection<MemberWisieFlow> flows) {
        super(container);
        this.flows = flows;
    }

    @Override
    public void execute() {
        for (GameFlow flow : flows) {
            flow.start();
        }
    }
}
