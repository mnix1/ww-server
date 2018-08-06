package com.ww.websocket;

import com.ww.model.container.ProfileConnection;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileConnectionService profileConnectionService;

    @Autowired
    BattleService battleService;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        logger.error("error occured at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        profileConnectionService.deleteConnection(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("Connected: sessionId: " + session.getId());
        ProfileConnection profileConnection =profileConnectionService.newConnection(session);
        battleService.sendActualBattleModelToNewProfileConnection(profileConnection);
    }

//    private ProfileConnection findProfileConnection(WebSocketSession session) {
//        return profileConnectionService.findBySessionId(session.getId());
//    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        String message = jsonTextMessage.getPayload();
        logger.debug("Message received: " + jsonTextMessage.getPayload() + ", from sessionId: " + session.getId());
        if (message.equals("BATTLE_READY_FOR_START")) {
            battleService.readyForStart(session.getId());
        } else if (message.contains("BATTLE_ANSWER")) {
            battleService.answer(session.getId(), message.substring("BATTLE_ANSWER".length()));
        }
    }
}