package com.ww.play.container;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Set;
import java.util.stream.Collectors;

public class PlayWarContainer extends PlayContainer {
    public PlayWarContainer(RivalTwoPlayerInit init, RivalTeams teams) {
        super(init, teams);
    }

    @Override
    public boolean isEnd() {
        for (RivalTeam team : teams.getTeams()) {
            if (!((WarTeam) team).isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRandomTaskProps() {
        Set<Integer> presentMemberCounts = teams.getTeams().stream().map(team -> ((WarTeam) team).countPresentMembers()).collect(Collectors.toSet());
        return presentMemberCounts.size() == 1;
    }

}
