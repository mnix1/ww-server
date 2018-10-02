package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalTeams;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class WarTeams extends RivalTeams {

    private final Map<Long, WarTeam> teamContainerMap = new HashMap<>();

    public void addProfile(Long id, WarTeam profileContainer) {
        this.getTeamContainerMap().put(id, profileContainer);
    }

    public WarTeam opponentTeamContainer(Long profileId) {
        return teamContainer(getOpponents().get(profileId));
    }


    public WarTeam teamContainer(Long profileId) {
        return teamContainerMap.get(profileId);
    }

    public Map<Long, WarTeam> getTeamContainerMap() {
        return teamContainerMap;
    }

    public Collection<WarTeam> getTeamContainers() {
        return this.getTeamContainerMap().values();
    }

}
