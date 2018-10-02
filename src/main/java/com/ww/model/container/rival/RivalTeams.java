package com.ww.model.container.rival;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public abstract class RivalTeams {
    private final Map<Long, Long> opponents = new HashMap<>();

    public abstract Map<Long, ? extends RivalTeam> getTeamContainerMap();

    public abstract RivalTeam teamContainer(Long profileId);

    public abstract RivalTeam opponentTeamContainer(Long profileId);

    public void forEachProfile(Consumer<? super RivalTeam> action){
        getTeamContainers().parallelStream().forEach(action);
    }

    public abstract Collection<? extends RivalTeam> getTeamContainers();
}
