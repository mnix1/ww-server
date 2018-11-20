package com.ww.model.container;

import com.ww.game.auto.AutoManager;
import com.ww.helper.TagHelper;
import com.ww.model.entity.inside.social.Auto;
import com.ww.service.rival.global.RivalMessageService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.ModelHelper.parseMessage;

@Getter
public class AutoProfileConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(AutoProfileConnection.class);

    private final RivalMessageService messageService;
    private final AutoManager manager;
    private final String sessionId;

    public AutoProfileConnection(RivalMessageService messageService, AutoManager manager) {
        this.messageService = messageService;
        this.manager = manager;
        this.sessionId = TagHelper.randomUUID();
    }

    @Override
    public Long getProfileId() {
        return manager.getProfile().getId();
    }

    @Override
    public String getProfileTag() {
        return manager.getProfile().getTag();
    }

    public void close() {
    }

    public void sendMessage(String msg) {
//        logger.trace(toString() + ", " + msg);
        manager.getCommunication().handleMessage(parseMessage(msg));
    }

    public void handleMessage(String msg) {
        messageService.handleMessage(sessionId, msg);
    }

    @Override
    public boolean equals(Object obj) {
        return getProfileId().equals(((Connection) obj).getProfileId());
    }

    @Override
    public String toString() {
        return "AutoProfileConnection{" +
                "profileId='" + getProfileId() + '\'' +
                "sessionId='" + sessionId + '\'' +
                super.toString() +
                '}';
    }
}