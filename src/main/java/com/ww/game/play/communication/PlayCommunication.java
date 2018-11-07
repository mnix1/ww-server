package com.ww.game.play.communication;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.*;
import com.ww.game.play.container.PlayContainer;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.service.rival.global.RivalMessageService.*;

public class PlayCommunication {
    protected PlayManager manager;
    private Message messageContent;
    protected Map<String, PlayAction> actionMap = new ConcurrentHashMap<>();
    protected Map<Long, Map<String, Object>> modelMap = new ConcurrentHashMap<>();

    public PlayCommunication(PlayManager manager, Message messageContent) {
        this.manager = manager;
        this.messageContent = messageContent;
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

    public void sendAndUpdateModel(Long profileId, Map<String, Object> model) {
        if (model.isEmpty()) {
            return;
        }
        send(profileId, model);
        Map<String, Object> actualProfileModel;
        if (!modelMap.containsKey(profileId)) {
            actualProfileModel = new HashMap<>();
            modelMap.put(profileId, actualProfileModel);
        } else {
            actualProfileModel = modelMap.get(profileId);
        }
        for (String key : model.keySet()) {
            actualProfileModel.put(key, model.get(key));
        }
    }

    public void send(Long profileId, Map<String, Object> model) {
        ProfileConnectionService profileConnectionService = manager.getProfileConnectionService();
        profileConnectionService.send(profileId, model, getMessageContent());
    }

//    public void sendChild() {
//        ProfileConnectionService profileConnectionService = manager.getProfileConnectionService();
//        GameState state = getContainer().currentState();
//        RivalTeams teams = getContainer().getTeams();
//        teams.forEachTeam(team -> {
//            Map<String, Object> model = state.prepareChildModel(team, teams.opponent(team.getProfileId()));
//            profileConnectionService.send(team.getProfileId(), model, getMessageContent());
//        });
//    }

//    public void sendModelFromBeginning(RivalTeam team, RivalTeam opponentTeam) {
//        Map<String, Object> model = new HashMap<>();
//        for (int i = getContainer().getStates().size() - 1; i >= 0; i--) {
//            GameState state = getContainer().getStates().get(i);
//            Map<String, Object> stateModel = state.prepareModel(team, opponentTeam);
//            for (String key : stateModel.keySet()) {
//                if (!model.containsKey(key)) {
//                    model.put(key, stateModel.get(key));
//                }
//            }
//        }
//        manager.getProfileConnectionService().send(team.getProfileId(), model, getMessageContent());
//    }

    public Message getMessageContent() {
        return messageContent;
    }

    public synchronized boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        if (actionMap.containsKey(id)) {
            actionMap.get(id).perform(profileId, content);
            return true;
        }
        return false;
    }
}
