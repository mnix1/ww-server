package com.ww.model.dto.rival;

import com.ww.model.constant.wisie.HeroType;
import com.ww.model.container.rival.war.TeamMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDTO {
    private int index;
    private HeroType type;
    private Object content;

    public TeamMemberDTO(TeamMember teamMember) {
        this.index = teamMember.getIndex();
        this.type = teamMember.getType();
        this.content = teamMember.getContentDTO();
    }
}
