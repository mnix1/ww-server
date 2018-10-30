package com.ww.play.container;

import com.ww.model.container.rival.RivalTasks;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.RivalTimeouts;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.WarTeam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayWarContainer extends PlayContainer {
    public PlayWarContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts) {
        super(init, teams, tasks, timeouts);
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

    @Override
    public String findChoosingTaskPropsTag() {
        List<WarTeam> presentMemberCounts = teams.getTeams().stream().map(team -> ((WarTeam) team)).collect(Collectors.toList());
        int minIndex = IntStream.range(0, presentMemberCounts.size())
                .reduce((i, j) -> presentMemberCounts.get(i).countPresentMembers() > presentMemberCounts.get(j).countPresentMembers() ? j : i)
                .getAsInt();
        return presentMemberCounts.get(minIndex).getProfile().getTag();
    }

}
