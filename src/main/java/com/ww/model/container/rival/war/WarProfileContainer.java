package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WarProfileContainer extends RivalProfileContainer {
    private List<TeamMember> teamMembers;
    private int activeIndex;
    private boolean isChosenActiveIndex;
    private int hints;

    public WarProfileContainer(Profile profile, List<TeamMember> teamMembers) {
        super(profile);
        this.teamMembers = teamMembers;
        this.activeIndex = 0;
        this.hints = 1;
    }

    public void setActiveTeamMemberPresentToFalse() {
        teamMembers.get(activeIndex).setPresent(false);
    }

    public TeamMember getActiveTeamMember() {
        return teamMembers.get(activeIndex);
    }

    public int countPresentMembers() {
        return getPresentIndexes().size();
    }

    public boolean isAnyPresentMember() {
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.isPresent()) {
                return true;
            }
        }
        return false;
    }

    public void decreaseHints(){
        hints--;
    }

    public List<Integer> getPresentIndexes(){
        List<Integer> indexes = new ArrayList<>();
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.isPresent()) {
                indexes.add(teamMember.getIndex());
            }
        }
        return indexes;
    }

}
