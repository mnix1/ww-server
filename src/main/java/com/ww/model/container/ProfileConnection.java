package com.ww.model.container;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Getter
@Setter
public class ProfileConnection {
    private static final Logger logger = LoggerFactory.getLogger(ProfileConnection.class);

    private Long profileId;
    private String profileTag;

    private WebSocketSession webSocketSession;

    public ProfileConnection(Long profileId, String profileTag, WebSocketSession webSocketSession) {
        this.profileId = profileId;
        this.profileTag = profileTag;
        this.webSocketSession = webSocketSession;
    }

    public String getSessionId() {
        return webSocketSession.getId();
    }

    public void close() {
        try {
            webSocketSession.close();
        } catch (IOException e) {
        }
    }

    public boolean sendMessage(String msg) {
        try {
            logger.trace("Send message to {}: {}", profileId, msg);
            webSocketSession.sendMessage(new TextMessage(msg));
            return true;
        } catch (Exception e) {
            logger.error("Error on websocket sending to {}: {}", profileId, msg);
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return profileId.equals(((ProfileConnection) obj).profileId);
    }

}