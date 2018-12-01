package com.ww.game.play.container;

import com.ww.helper.JSONHelper;
import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

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
    protected Map<Long, Map<String, Object>> newModelMap = new ConcurrentHashMap<>();

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
        return super.toString() + "{" +
                "init=" + init +
                ", teams=" + teams +
                ", tasks=" + tasks +
                ", timeouts=" + timeouts +
                ", decisions=" + decisions +
                ", result=" + result +
                '}';
    }

    public abstract boolean isEnd();

    public abstract boolean isRandomTaskProps();

    public abstract Profile findChoosingTaskPropsProfile();

    public abstract Optional<Profile> findWinner();

    public boolean isRanking() {
        return init.getImportance().isRanking();
    }

    public boolean isFriend() {
        return init.getImportance().isFriend();
    }
    public boolean isTraining() {
        return init.getImportance().isTraining();
    }

    public boolean isDraw() {
        return result.getDraw();
    }

    private void initModelMaps() {
        for (RivalTeam team : getTeams().getTeams()) {
            Long profileId = team.getProfileId();
            actualModelMap.put(profileId, new HashMap<>());
            newModelMap.put(profileId, new HashMap<>());
            allModelMap.put(profileId, new ArrayList<>());
        }
    }

    public void updateModels(Long profileId, Map<String, Object> model) {
        Map<String, Object> actualProfileModel = actualModelMap.get(profileId);
        Map<String, Object> newProfileModel = newModelMap.get(profileId);
        for (String key : model.keySet()) {
            actualProfileModel.put(key, model.get(key));
            newProfileModel.put(key, model.get(key));
        }
        allModelMap.get(profileId).add(JSONHelper.toJSON(model));
    }

    public Map<String, Object> modelToSend(Long profileId) {
        return newModelMap.get(profileId);
    }

    public void cleanModelToSend(Long profileId) {
        newModelMap.put(profileId, new HashMap<>());
    }

    public String modelsToJSON() {
        return JSONHelper.toJSON(allModelMap);
    }
}
