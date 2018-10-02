package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalTeamsContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BattleTeamsContainer extends RivalTeamsContainer {

    private final Map<Long, BattleTeamContainer> profileContainerMap = new HashMap<>();

    public void addProfile(Long id, BattleTeamContainer profileContainer) {
        getTeamContainerMap().put(id, profileContainer);
    }

    public BattleTeamContainer opponentTeamContainer(Long profileId) {
        return teamContainer(getOpponents().get(profileId));
    }

    public BattleTeamContainer teamContainer(Long profileId) {
        return profileContainerMap.get(profileId);
    }

    public Map<Long, BattleTeamContainer> getTeamContainerMap() {
        return profileContainerMap;
    }

    public Collection<BattleTeamContainer> getTeamContainers() {
        return getTeamContainerMap().values();
    }

}
