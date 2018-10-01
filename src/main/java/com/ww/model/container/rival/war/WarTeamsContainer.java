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

    private final Map<Long, WarProfileContainer> profileContainerMap = new HashMap<>();

    public void addProfile(Long id, WarProfileContainer profileContainer) {
        getProfileContainerMap().put(id, profileContainer);
    }

    public WarProfileContainer opponentProfileContainer(Long profileId) {
        return profileContainer(getOpponents().get(profileId));
    }


    public WarProfileContainer profileContainer(Long profileId) {
        return profileContainerMap.get(profileId);
    }

    public Map<Long, WarProfileContainer> getProfileContainerMap() {
        return profileContainerMap;
    }

    public Collection<WarProfileContainer> getProfileContainers() {
        return getProfileContainerMap().values();
    }

}
