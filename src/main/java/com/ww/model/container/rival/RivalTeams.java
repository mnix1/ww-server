package com.ww.model.container.rival;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public abstract class RivalTeams {
    private final Map<Long, Long> opponentMap = new HashMap<>();

    public abstract Map<Long, ? extends RivalTeam> getTeamMap();

    public abstract RivalTeam team(Long profileId);

    public abstract RivalTeam opponentTeam(Long profileId);

    public void forEachTeam(Consumer<? super RivalTeam> action){
        getTeams().parallelStream().forEach(action);
    }

    public abstract Collection<? extends RivalTeam> getTeams();
}
