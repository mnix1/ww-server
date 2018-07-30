package com.ww.websocket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class ProfileConnection {

    private Long profileId;

    private WebSocketSession webSocketSession;

    private boolean isReady;

    public ProfileConnection(Long profileId, WebSocketSession webSocketSession) {
        this.profileId = profileId;
        this.webSocketSession = webSocketSession;
    }

    String getSessionId() {
        return webSocketSession.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return profileId.equals(((ProfileConnection) obj).profileId);
    }

}