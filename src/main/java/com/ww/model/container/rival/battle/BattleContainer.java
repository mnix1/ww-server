package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class BattleContainer extends RivalContainer {

    public String findChoosingTaskPropsTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer p1Score =  ((BattleProfileContainer)rivalProfileContainers.get(0)).getScore();
        Integer p2Score =  ((BattleProfileContainer)rivalProfileContainers.get(1)).getScore();
        if (p1Score.equals(p2Score)) {
            return null;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return rivalProfileContainers.get(0).getProfile().getTag();
        }
        return rivalProfileContainers.get(1).getProfile().getTag();
    }

    public Optional<Profile> findWinner() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer p1Score = ((BattleProfileContainer)rivalProfileContainers.get(0)).getScore();
        Integer p2Score = ((BattleProfileContainer) rivalProfileContainers.get(1)).getScore();
        if (p1Score.equals(p2Score)) {
            return Optional.empty();
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return Optional.of(rivalProfileContainers.get(1).getProfile());
        }
        return Optional.of(rivalProfileContainers.get(0).getProfile());
    }

}
