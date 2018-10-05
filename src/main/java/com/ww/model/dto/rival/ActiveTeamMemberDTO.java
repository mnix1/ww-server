package com.ww.model.dto.rival;

import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.container.rival.war.TeamMember;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ActiveTeamMemberDTO {
    private boolean present;
    private DisguiseType disguise;

    public ActiveTeamMemberDTO(TeamMember teamMember) {
        this.present = teamMember.isPresent();
        this.disguise = teamMember.activeDisguise();
    }
}
