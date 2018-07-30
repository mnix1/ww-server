package com.ww.websocket;

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
        profileConnectionService.newConnection(session);
    }

    private ProfileConnection findProfileConnection(WebSocketSession session) {
        return profileConnectionService.findBySessionId(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        String message = jsonTextMessage.getPayload();
        logger.debug("Message received: " + jsonTextMessage.getPayload() + ", from sessionId: " + session.getId());
//        if (message.contains("CONTROL")) {
//            battleService.move(session.getId(), message.substring(7));
//        } else if (message.equals("BATTLE_START")) {
//            BattleWrapper battleWrapper = battleService.createBattle(findProfileConnection(session));
//            if (battleWrapper.isPreparingStatus()) {
//                battleService.prepareBattle(battleWrapper);
//                logger.debug("Preparing Battle");
//            }
//        } else if (message.equals("BATTLE_CANCEL")) {
//            battleService.cancelBattle(findProfileConnection(session));
//        } else if (message.equals("READY_FOR_START")) {
//            battleService.readyForStart(session.getId());
//        }
    }
}