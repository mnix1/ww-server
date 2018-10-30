package com.ww.model.container.rival;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@NoArgsConstructor
public class RivalTeams {
    private final Map<Long, RivalTeam> teamMap = new ConcurrentHashMap<>();
    private final Map<Long, Long> opponentMap = new ConcurrentHashMap<>();

    public RivalTeams(RivalTeam team1, RivalTeam team2) {
        addTeams(team1, team2);
    }

    public void addTeams(RivalTeam team1, RivalTeam team2) {
        teamMap.put(team1.getProfileId(), team1);
        teamMap.put(team2.getProfileId(), team2);
        opponentMap.put(team1.getProfileId(), team2.getProfileId());
        opponentMap.put(team2.getProfileId(), team1.getProfileId());
    }

    public RivalTeam team(Long profileId) {
        return teamMap.get(profileId);
    }

    public RivalTeam opponent(Long profileId) {
        return team(opponentMap.get(profileId));
    }

    public RivalTeam opponent(RivalTeam team) {
        return team(opponentMap.get(team.getProfileId()));
    }

    public void forEachTeam(Consumer<? super RivalTeam> action) {
        getTeams().parallelStream().forEach(action);
    }

    public Collection<RivalTeam> getTeams() {
        return teamMap.values();
    }
}
