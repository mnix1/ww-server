package com.ww.game.member;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.game.play.PlayManager;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import lombok.Getter;

@Getter
public class MemberWisieManager {
    protected PlayManager playManager;

    protected MemberWisieContainer container;
    protected MemberWisieFlow flow;

    public MemberWisieManager(WarTeam team, WisieTeamMember member, PlayManager playManager) {
        this.playManager = playManager;
        this.container = new MemberWisieContainer(team, member, playManager.getContainer().getTasks().question());
        this.flow = new MemberWisieFlow(this);
    }

    public void start() {
        flow.run("WAITING_FOR_QUESTION");
    }
    public void stop() {
        flow.stop();
    }
}
