package com.ww.play.container;

import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.entity.outside.social.Profile;

import java.util.List;
import java.util.Optional;

import static com.ww.helper.TeamHelper.*;

public class PlayWarContainer extends PlayContainer {
    public PlayWarContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        super(init, teams, tasks, timeouts, decisions, result);
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
        return teamsHaveSameCountPresentMembers(teams.getTeams());
    }

    @Override
    public Profile findChoosingTaskPropsProfile() {
        List<WarTeam> warTeams = mapToWarTeams(teams.getTeams());
        return teamWithLowestCountPresentMembers(warTeams).getProfile();
    }

    @Override
    public Optional<Profile> findWinner() {
        boolean isDraw = teamsHaveSameCountPresentMembers(teams.getTeams());
        if (isDraw) {
            return Optional.empty();
        }
        List<WarTeam> warTeams = mapToWarTeams(teams.getTeams());
        WarTeam looser = teamWithLowestCountPresentMembers(warTeams);
        return Optional.of(teams.opponent(looser).getProfile());
    }

}
