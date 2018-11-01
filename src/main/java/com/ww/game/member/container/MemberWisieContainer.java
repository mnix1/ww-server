package com.ww.game.member.container;

import com.ww.game.GameContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WisieTeamMember;
import lombok.Getter;

public class MemberWisieContainer extends MemberContainer {
    @Getter
    private WisieTeamMember member;

    public MemberWisieContainer(WisieTeamMember member) {
        this.member = member;
    }
}
