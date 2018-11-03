package com.ww.game.member;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.game.play.PlayManager;
import com.ww.model.container.rival.war.WisieTeamMember;

public class MemberWisieManager extends MemberManager {
    public MemberWisieManager(WisieTeamMember member, PlayManager playManager) {
        this.playManager = playManager;
        this.container = new MemberWisieContainer(member, playManager.getContainer().getTasks().question());
        this.flow = new MemberWisieFlow(this);
    }

    public void start() {
        flow.start();
    }
}
