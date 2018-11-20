package com.ww.game.auto.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.game.auto.AutoManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.websocket.message.Message;
import lombok.Getter;

import java.util.Map;

import static com.ww.helper.ModelHelper.parseMessage;

@Getter
public class AutoCommunication {
    private AutoManager manager;

    public AutoCommunication(AutoManager manager) {
        this.manager = manager;
    }

    public void send(String id, Map<String, Object> params) {
        params.put("id", id);
        try {
            String msg = new ObjectMapper().writeValueAsString(params);
            manager.getConnection().handleMessage(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(Map<String, Object> model) {
        Message id = Message.valueOf((String) model.get("id"));
        if (handleRivalContent(id, model)) {
            return;
        }
    }

    private boolean handleRivalContent(Message id, Map<String, Object> model) {
        if (id != Message.RIVAL_CONTENT) {
            return false;
        }
        Map<String, Object> content = parseMessage((String) model.get("content"));
        if (!content.containsKey("status")) {
            return false;
        }
        RivalStatus status = RivalStatus.valueOf((String) content.get("status"));
        if (status == RivalStatus.INTRO) {
            manager.getFlow().run("RIVAL_INTRO");
        } else if (status == RivalStatus.CHOOSING_WHO_ANSWER) {
            manager.getFlow().run("RIVAL_CHOOSING_WHO_ANSWER");
        } else if (status == RivalStatus.CHOOSING_TASK_CATEGORY) {
            manager.getFlow().run("RIVAL_CHOOSING_TASK_CATEGORY");
        } else if (status == RivalStatus.CHOOSING_TASK_DIFFICULTY) {
            manager.getFlow().run("RIVAL_CHOOSING_TASK_DIFFICULTY");
        }
        return true;
    }
}
