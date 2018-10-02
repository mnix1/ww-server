package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class BattleContainer extends RivalContainer {

    private BattleTeamsContainer teamsContainer;

    public BattleContainer(RivalInitContainer container, BattleTeamsContainer teamsContainer) {
        super(container, teamsContainer);
        this.teamsContainer = teamsContainer;
    }

    public String findChoosingTaskPropsTag() {
        List<BattleTeamContainer> profileContainers = new ArrayList<>(getTeamsContainer().getTeamContainers());
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
        List<BattleTeamContainer> profileContainers = new ArrayList<>(getTeamsContainer().getTeamContainers());
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
