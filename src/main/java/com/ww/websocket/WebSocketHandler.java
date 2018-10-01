package com.ww.websocket;

import com.ww.model.container.ProfileConnection;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.global.RivalMessageService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private RivalMessageService rivalMessageService;

    @Autowired
    private RivalGlobalService rivalGlobalService;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) {
        logger.error("Error occurred at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.debug(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        Optional<ProfileConnection> optionalProfileConnection = profileConnectionService.findBySessionId(session.getId());
        if (!optionalProfileConnection.isPresent()) {
            return;
        }
        ProfileConnection profileConnection = optionalProfileConnection.get();
        profileConnectionService.deleteConnection(profileConnection);
        profileConnectionService.sendFriendConnectionChanged(profileConnection.getProfileTag(), Message.FRIEND_SIGN_OUT);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("Connected: sessionId: " + session.getId());
        ProfileConnection profileConnection = profileConnectionService.newConnection(session);
        profileConnectionService.sendFriendConnectionChanged(profileConnection.getProfileTag(), Message.FRIEND_SIGN_IN);
        rivalGlobalService.sendActualRivalModelToNewProfileConnection(profileConnection);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) {
        String message = jsonTextMessage.getPayload();
        rivalMessageService.handleMessage(session.getId(), message);
    }
}