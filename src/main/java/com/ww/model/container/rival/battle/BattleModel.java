package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class BattleModel extends RivalModel {

    private RivalTeams teams;

    public BattleModel(RivalInit init, RivalTeams teams) {
        super(init, teams);
        this.teams = teams;
    }

    public String findChoosingTaskPropsTag() {
        List<BattleTeam> profileContainers = new ArrayList<>(this.getTeams().getTeams()).stream().map(team -> (BattleTeam) team).collect(Collectors.toList());
        Integer p1Score = profileContainers.get(0).getScore();
        Integer p2Score = profileContainers.get(1).getScore();
        if (p1Score.equals(p2Score)) {
            return null;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return profileContainers.get(0).getProfile().getTag();
        }
        return profileContainers.get(1).getProfile().getTag();
    }

    public Optional<Profile> findWinner() {
        List<BattleTeam> profileContainers = new ArrayList<>(this.getTeams().getTeams()).stream().map(team -> (BattleTeam) team).collect(Collectors.toList());
        Integer p1Score = profileContainers.get(0).getScore();
        Integer p2Score = profileContainers.get(1).getScore();
        if (p1Score.equals(p2Score)) {
            return Optional.empty();
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return Optional.of(profileContainers.get(1).getProfile());
        }
        return Optional.of(profileContainers.get(0).getProfile());
    }

}
