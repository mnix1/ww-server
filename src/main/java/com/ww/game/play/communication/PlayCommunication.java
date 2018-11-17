package com.ww.game.play.communication;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.*;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void sendAndUpdateModel(Long profileId, Map<String, Object> model) {
        if (model.isEmpty()) {
            return;
        }
        fillModelNow(model);
        send(profileId, model);
        manager.getContainer().updateModels(profileId, model);
    }

    public void sendWithCurrentTime(Long profileId, Map<String, Object> model) {
        fillModelNow(model);
        send(profileId, model);
    }

    public void send(Long profileId, Map<String, Object> model) {
        ProfileConnectionService profileConnectionService = manager.getProfileConnectionService();
        profileConnectionService.send(profileId, model, Message.RIVAL_CONTENT);
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
