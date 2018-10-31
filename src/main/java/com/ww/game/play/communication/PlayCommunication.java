package com.ww.game.play.communication;

import com.ww.model.container.rival.RivalTeams;
import com.ww.game.play.PlayManager;
import com.ww.game.play.action.*;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayState;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.service.rival.global.RivalMessageService.*;

public class PlayCommunication {
    protected PlayManager manager;
    private Message messageContent;
    protected Map<String, PlayAction> actionMap = new ConcurrentHashMap<>();

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

    public void send() {
        ProfileConnectionService profileConnectionService = manager.getProfileConnectionService();
        PlayState state = getContainer().currentState();
        RivalTeams teams = getContainer().getTeams();
        teams.forEachTeam(team -> {
            Map<String, Object> model = state.prepareAndStoreModel(team, teams.opponent(team.getProfileId()));
            profileConnectionService.send(team.getProfileId(), model, getMessageContent());
        });
    }

    public void sendModelFromBeginning(Long profileId) {
        List<PlayState> states = getContainer().getStates();
        Map<String, Object> model = new HashMap<>();
        for (PlayState state : states) {
            state.getStoredModel(profileId).ifPresent(model::putAll);
        }
        manager.getProfileConnectionService().send(profileId, model, getMessageContent());
    }

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
