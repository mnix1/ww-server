package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalTeams;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BattleTeams extends RivalTeams {

    private final Map<Long, BattleTeam> profileContainerMap = new HashMap<>();

    public void addProfile(Long id, BattleTeam profileContainer) {
        getTeamContainerMap().put(id, profileContainer);
    }

    public BattleTeam opponentTeamContainer(Long profileId) {
        return teamContainer(getOpponents().get(profileId));
    }

    public BattleTeam teamContainer(Long profileId) {
        return profileContainerMap.get(profileId);
    }

    public Map<Long, BattleTeam> getTeamContainerMap() {
        return profileContainerMap;
    }

    public Collection<BattleTeam> getTeamContainers() {
        return getTeamContainerMap().values();
    }

}
