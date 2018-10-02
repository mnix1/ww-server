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
        getTeamMap().put(id, profileContainer);
    }

    public BattleTeam opponentTeam(Long profileId) {
        return team(getOpponentMap().get(profileId));
    }

    public BattleTeam team(Long profileId) {
        return profileContainerMap.get(profileId);
    }

    public Map<Long, BattleTeam> getTeamMap() {
        return profileContainerMap;
    }

    public Collection<BattleTeam> getTeams() {
        return getTeamMap().values();
    }

}
