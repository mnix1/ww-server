package com.ww.websocket;

import com.ww.model.container.Connection;
import com.ww.model.container.ProfileConnection;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.global.RivalMessageService;
import com.ww.service.social.ConnectionService;
import com.ww.websocket.message.Message;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private final ConnectionService connectionService;
    private final RivalMessageService rivalMessageService;
    private final RivalGlobalService rivalGlobalService;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) {
        logger.error("Error occurred at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.debug(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        Optional<Connection> optionalProfileConnection = connectionService.findBySessionId(session.getId());
        if (!optionalProfileConnection.isPresent()) {
            return;
        }
        Connection profileConnection = optionalProfileConnection.get();
        connectionService.deleteConnection(profileConnection);
        connectionService.sendFriendConnectionChanged(profileConnection.getProfileTag(), Message.FRIEND_SIGN_OUT);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("Connected: sessionId: " + session.getId());
        ProfileConnection profileConnection = connectionService.newProfileConnection(session);
        connectionService.sendFriendConnectionChanged(profileConnection.getProfileTag(), Message.FRIEND_SIGN_IN);
        rivalGlobalService.sendActualRivalModelToNewProfileConnection(profileConnection);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) {
        String message = jsonTextMessage.getPayload();
        rivalMessageService.handleMessage(session.getId(), message);
    }
}