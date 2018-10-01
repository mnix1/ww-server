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

    private final Map<Long, BattleProfileContainer> profileContainerMap = new HashMap<>();

    public void addProfile(Long id, BattleProfileContainer profileContainer) {
        getProfileContainerMap().put(id, profileContainer);
    }

    public BattleProfileContainer opponentProfileContainer(Long profileId) {
        return profileContainer(getOpponents().get(profileId));
    }

    public BattleProfileContainer profileContainer(Long profileId) {
        return profileContainerMap.get(profileId);
    }

    public Map<Long, BattleProfileContainer> getProfileContainerMap() {
        return profileContainerMap;
    }

    public Collection<BattleProfileContainer> getProfileContainers() {
        return getProfileContainerMap().values();
    }

}
