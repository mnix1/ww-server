package com.ww.game.play.container;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.*;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import org.apache.commons.lang3.builder.Diff;

import java.util.List;
import java.util.Optional;

import static com.ww.helper.TeamHelper.*;
import static com.ww.game.play.PlayBattleManager.TASK_COUNT;

public class PlayBattleContainer extends PlayContainer {
    public PlayBattleContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        super(init, teams, tasks, timeouts, decisions, result);
    }

    @Override
    public boolean isEnd() {
        int leftTasks = TASK_COUNT - tasks.nextTaskIndex();
        if (leftTasks == 0) {
            return true;
        }
        int maxScoreToGet = leftTasks * DifficultyLevel.EXTREMELY_HARD.getPoints();
        return maxScoreToGet < minDifferenceInTeamScores();
    }

    private int minDifferenceInTeamScores() {
        int minDifference = Integer.MAX_VALUE;
        List<BattleTeam> battleTeams = mapToBattleTeams(teams.getTeams());
        for (int i = 0; i < battleTeams.size(); i++) {
            BattleTeam team = battleTeams.get(i);
            for (int j = i + 1; j < battleTeams.size(); j++) {
                BattleTeam teamToCompare = battleTeams.get(j);
                minDifference = Math.min(minDifference, Math.abs(team.getScore() - teamToCompare.getScore()));
            }
        }
        return minDifference;
    }

    @Override
    public boolean isRandomTaskProps() {
        return teamsHaveSameScore(teams.getTeams());
    }

    @Override
    public Profile findChoosingTaskPropsProfile() {
        List<BattleTeam> battleTeams = mapToBattleTeams(teams.getTeams());
        return teamWithLowestScore(battleTeams).getProfile();
    }

    @Override
    public Optional<Profile> findWinner() {
        boolean isDraw = teamsHaveSameScore(teams.getTeams());
        if (isDraw) {
            return Optional.empty();
        }
        List<BattleTeam> battleTeams = mapToBattleTeams(teams.getTeams());
        BattleTeam looser = teamWithLowestScore(battleTeams);
        return Optional.of(teams.opponent(looser).getProfile());
    }

}
