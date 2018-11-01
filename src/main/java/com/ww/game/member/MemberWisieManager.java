package com.ww.game.member;

import com.ww.game.member.communication.MemberCommunication;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.model.container.rival.war.WisieTeamMember;

public class MemberWisieManager extends MemberManager {
    public MemberWisieManager(WisieTeamMember member) {
        this.container = new MemberWisieContainer(member);
        this.flow = new MemberWisieFlow(this);
        this.communication = new MemberCommunication();
    }

    public void start() {
        flow.start();
    }
}
