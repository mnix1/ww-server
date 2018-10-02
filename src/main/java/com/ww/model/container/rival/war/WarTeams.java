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

    private final Map<Long, WarTeam> teamMap = new HashMap<>();

    public void addProfile(Long id, WarTeam profileContainer) {
        this.getTeamMap().put(id, profileContainer);
    }

    public WarTeam opponentTeam(Long profileId) {
        return team(getOpponentMap().get(profileId));
    }

    public WarTeam team(Long profileId) {
        return teamMap.get(profileId);
    }

    public Map<Long, WarTeam> getTeamMap() {
        return teamMap;
    }

    public Collection<WarTeam> getTeams() {
        return this.getTeamMap().values();
    }

}
