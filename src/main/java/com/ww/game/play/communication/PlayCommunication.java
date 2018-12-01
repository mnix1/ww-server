package com.ww.game.play.communication;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.*;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.modelfiller.PlayModelPreparer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.service.social.ConnectionService;
import com.ww.websocket.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNow;
import static com.ww.service.rival.global.RivalMessageService.*;

public class PlayCommunication {
    protected PlayManager manager;
    protected Map<String, PlayAction> actionMap = new ConcurrentHashMap<>();

    public PlayCommunication(PlayManager manager) {
        this.manager = manager;
        initActionMap();
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    protected void initActionMap() {
        actionMap.put(SURRENDER, new PlaySurrenderAction(manager));
        actionMap.put(ANSWER, new PlayAnswerAction(manager));
        actionMap.put(CHOOSE_TASK_CATEGORY, new PlayChooseTaskCategoryAction(manager));
        actionMap.put(CHOOSE_TASK_DIFFICULTY, new PlayChooseTaskDifficultyAction(manager));
    }

    public synchronized void prepareModel(PlayModelPreparer modelPreparer) {
        RivalTeams teams = getContainer().getTeams();
        for (RivalTeam team : teams.getTeams()) {
            Map<String, Object> model = modelPreparer.prepareModel(team, teams.opponent(team));
            if (model.isEmpty()) {
                continue;
            }
            fillModelNow(model);
            getContainer().updateModels(team.getProfileId(), model);
        }
    }

    public synchronized void sendPreparedModel() {
        RivalTeams teams = getContainer().getTeams();
        teams.forEachTeam(team -> {
            Map<String, Object> model = getContainer().modelToSend(team.getProfileId());
            if (model.isEmpty()) {
                return;
            }
            manager.send(team.getProfileId(), model);
            getContainer().cleanModelToSend(team.getProfileId());
        });
    }

    public void sendWithCurrentTime(Long profileId, Map<String, Object> model) {
        fillModelNow(model);
        manager.send(profileId, model);
    }

    public void sendModelFromBeginning(Long profileId) {
        sendWithCurrentTime(profileId, getContainer().getActualModelMap().get(profileId));
    }

    public void processMessage(Long profileId, Map<String, Object> content) {
        try {
            String id = (String) content.get("id");
            if (actionMap.containsKey(id)) {
                actionMap.get(id).perform(profileId, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
