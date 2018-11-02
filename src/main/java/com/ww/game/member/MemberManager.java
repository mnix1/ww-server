package com.ww.game.member;

import com.ww.game.member.container.MemberContainer;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.game.play.PlayWarManager;
import lombok.Getter;

@Getter
public class MemberManager {
    protected PlayWarManager playManager;
    protected MemberContainer container;
    protected MemberWisieFlow flow;

}
