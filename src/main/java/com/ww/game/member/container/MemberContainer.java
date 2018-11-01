package com.ww.game.member.container;

import com.ww.game.GameContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WisieTeamMember;

public abstract class MemberContainer extends GameContainer {
    public abstract TeamMember getMember();
}
