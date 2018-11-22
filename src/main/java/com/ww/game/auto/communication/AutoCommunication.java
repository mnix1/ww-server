package com.ww.game.auto.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.game.auto.AutoManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.websocket.message.Message;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

import static com.ww.helper.ModelHelper.parseMessage;

@Getter
public class AutoCommunication {
    public static Logger logger = LoggerFactory.getLogger(AutoCommunication.class);
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
//        logger.trace(toString() + " handleMessage, id={} thread={}", id, Thread.currentThread().getName());
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
            manager.getFlow().run("RIVAL_MAYBE_SKILL");
            return true;
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
        } else if (status == RivalStatus.ANSWERING) {
            manager.getFlow().run("RIVAL_ANSWERING");
        } else if (status == RivalStatus.CLOSED) {
            manager.getFlow().run("RIVAL_CLOSED");
        } else if (status == RivalStatus.ANSWERED
                || status == RivalStatus.ANSWERING_TIMEOUT
                || status == RivalStatus.CHANGING_TASK) {
            manager.getFlow().run("RIVAL_STOP_ANSWERING");
        }
        return true;
    }

    @Override
    public String toString() {
        return "AutoCommunication{" +
                "manager=" + manager +
                '}';
    }
}
