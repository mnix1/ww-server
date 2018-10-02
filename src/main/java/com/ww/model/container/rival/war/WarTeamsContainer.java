package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalTeamsContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class WarTeamsContainer extends RivalTeamsContainer {

    private final Map<Long, WarTeamContainer> teamContainerMap = new HashMap<>();

    public void addProfile(Long id, WarTeamContainer profileContainer) {
        this.getTeamContainerMap().put(id, profileContainer);
    }

    public WarTeamContainer opponentTeamContainer(Long profileId) {
        return teamContainer(getOpponents().get(profileId));
    }


    public WarTeamContainer teamContainer(Long profileId) {
        return teamContainerMap.get(profileId);
    }

    public Map<Long, WarTeamContainer> getTeamContainerMap() {
        return teamContainerMap;
    }

    public Collection<WarTeamContainer> getTeamContainers() {
        return this.getTeamContainerMap().values();
    }

}
