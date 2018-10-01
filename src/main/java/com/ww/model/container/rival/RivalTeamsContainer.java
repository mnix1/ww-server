package com.ww.model.container.rival;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public abstract class RivalTeamsContainer {
    private final Map<Long, Long> opponents = new HashMap<>();

    public abstract Map<Long, ? extends RivalProfileContainer> getProfileContainerMap();

    public abstract RivalProfileContainer profileContainer(Long profileId);

    public abstract RivalProfileContainer opponentProfileContainer(Long profileId);

    public void forEachProfile(Consumer<? super RivalProfileContainer> action){
        getProfileContainers().parallelStream().forEach(action);
    }

    public abstract Collection<? extends RivalProfileContainer> getProfileContainers();
}
