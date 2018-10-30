package com.ww.play.communication;

import com.ww.model.container.rival.RivalTeams;
import com.ww.play.PlayManager;
import com.ww.play.command.PlayAnswerCommand;
import com.ww.play.command.PlayChooseTaskPropsCommand;
import com.ww.play.command.PlayCommand;
import com.ww.play.command.PlaySurrenderCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayState;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.service.rival.global.RivalMessageService.*;

public abstract class PlayCommunication {
    protected PlayManager manager;
    protected Map<String, PlayCommand> commandMap = new ConcurrentHashMap<>();

    protected PlayCommunication(PlayManager manager) {
        this.manager = manager;
        initCommandMap();
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    protected void initCommandMap() {
        commandMap.put(SURRENDER, new PlaySurrenderCommand(manager));
        commandMap.put(ANSWER, new PlayAnswerCommand(manager));
        commandMap.put(CHOOSE_TASK_PROPS, new PlayChooseTaskPropsCommand(manager));
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
        for (int i = states.size() - 1; i >= 0; i--) {
            PlayState state = states.get(i);
            state.getStoredModel(profileId).ifPresent(model::putAll);
        }
        manager.getProfileConnectionService().send(profileId, model, getMessageContent());
    }

    public abstract Message getMessageContent();

    public synchronized boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        if (commandMap.containsKey(id)) {
            commandMap.get(id).execute(profileId, content);
            return true;
        }
        return false;
    }
}
