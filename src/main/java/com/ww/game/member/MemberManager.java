package com.ww.game.member;

import com.ww.game.member.communication.MemberCommunication;
import com.ww.game.member.container.MemberContainer;
import com.ww.game.member.flow.MemberWisieFlow;
import lombok.Getter;

@Getter
public class MemberManager {
    protected MemberContainer container;
    protected MemberWisieFlow flow;
    protected MemberCommunication communication;

}
