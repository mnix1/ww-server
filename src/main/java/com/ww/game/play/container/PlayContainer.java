package com.ww.game.play.container;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class PlayContainer {
    protected RivalTwoInit init;
    protected RivalTeams teams;
    protected RivalTasks tasks;
    protected RivalTimeouts timeouts;
    protected RivalDecisions decisions;
    protected RivalResult result;

    protected Map<Long, Map<String, Object>> actualModelMap = new ConcurrentHashMap<>();
    protected Map<Long, List<String>> allModelMap = new ConcurrentHashMap<>();

    public PlayContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        this.init = init;
        this.teams = teams;
        this.tasks = tasks;
        this.timeouts = timeouts;
        this.decisions = decisions;
        this.result = result;
        initModelMaps();
    }

    @Override
    public String toString() {
        return super.toString() + ", " + init.toString();
    }

    public abstract boolean isEnd();

    public abstract boolean isRandomTaskProps();

    public abstract Profile findChoosingTaskPropsProfile();

    public abstract Optional<Profile> findWinner();

    public boolean isRanking() {
        return init.getImportance() == RivalImportance.RANKING;
    }

    private void initModelMaps() {
        for (RivalTeam team : getTeams().getTeams()) {
            Long profileId = team.getProfileId();
            actualModelMap.put(profileId, new HashMap<>());
            allModelMap.put(profileId, new ArrayList<>());
        }
    }

    public void updateModels(Long profileId, Map<String, Object> model) {
        Map<String, Object> actualProfileModel = actualModelMap.get(profileId);
        for (String key : model.keySet()) {
            actualProfileModel.put(key, model.get(key));
        }
        try {
            allModelMap.get(profileId).add(new ObjectMapper().writeValueAsString(model));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String modelsToJSON() {
        try {
            return new ObjectMapper().writeValueAsString(allModelMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
