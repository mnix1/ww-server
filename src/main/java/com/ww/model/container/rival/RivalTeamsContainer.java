package com.ww.model.container.rival;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public abstract class RivalTeamsContainer {
    private final Map<Long, Long> opponents = new HashMap<>();

    public abstract Map<Long, ? extends RivalTeamContainer> getTeamContainerMap();

    public abstract RivalTeamContainer teamContainer(Long profileId);

    public abstract RivalTeamContainer opponentTeamContainer(Long profileId);

    public void forEachProfile(Consumer<? super RivalTeamContainer> action){
        getTeamContainers().parallelStream().forEach(action);
    }

    public abstract Collection<? extends RivalTeamContainer> getTeamContainers();
}
