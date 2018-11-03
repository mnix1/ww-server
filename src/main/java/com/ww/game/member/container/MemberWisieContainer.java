package com.ww.game.member.container;

import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;

public class MemberWisieContainer extends MemberContainer {
    public MemberWisieContainer(WarTeam team, WisieTeamMember member, Question question) {
        super(team, member, question);
    }

    public WarTeam getTeam() {
        return (WarTeam) team;
    }

    public WisieTeamMember getMember() {
        return (WisieTeamMember) member;
    }
}
