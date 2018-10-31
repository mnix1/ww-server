package com.ww.game.member;

import com.ww.game.member.communication.MemberCommunication;
import com.ww.game.member.container.MemberContainer;
import com.ww.game.member.flow.MemberFlow;

public class MemberWisieManager extends MemberManager {
    public MemberWisieManager() {
        this.container = new MemberContainer();
        this.flow = new MemberFlow();
        this.communication = new MemberCommunication();
    }
}
