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

    public void sendModelFromBeginning(Long profileId) {
        send(profileId, modelMap.get(profileId));
    }

    public Message getMessageContent() {
        return messageContent;
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
