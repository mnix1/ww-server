package com.ww.websocket;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.service.rival.AbstractRivalService;
import com.ww.service.rival.GlobalRivalService;
import com.ww.service.rival.RivalChallengeService;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.war.RivalWarService;
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
    RivalBattleService rivalBattleService;

    @Autowired
    RivalWarService rivalWarService;

    @Autowired
    GlobalRivalService globalRivalService;

    @Autowired
    RivalCampaignWarService rivalCampaignWarService;

    @Autowired
    RivalChallengeService rivalChallengeService;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        logger.error("error occurred at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        profileConnectionService.deleteConnection(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("Connected: sessionId: " + session.getId());
        ProfileConnection profileConnection = profileConnectionService.newConnection(session);
        globalRivalService.sendActualRivalModelToNewProfileConnection(profileConnection);
    }

    private static final String ANSWER_SUFFIX = "_^_ANSWER";
    private static final String CHOOSE_TASK_PROPS_SUFFIX = "_^_CHOOSE_TASK_PROPS";
    private static final String SURRENDER_SUFFIX = "_^_SURRENDER";
    private static final String CHOOSE_WHO_ANSWER_SUFFIX = "_^_CHOOSE_WHO_ANSWER";

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        String message = jsonTextMessage.getPayload();
//        logger.debug("Message received: " + jsonTextMessage.getPayload() + ", from sessionId: " + session.getId());
        AbstractRivalService abstractRivalService = null;
        RivalType rivalType = null;
        if (message.startsWith(RivalType.BATTLE.name())) {
            abstractRivalService = rivalBattleService;
            rivalType = RivalType.BATTLE;
        } else if (message.startsWith(RivalType.WAR.name())) {
            abstractRivalService = rivalWarService;
            rivalType = RivalType.WAR;
        } else if (message.startsWith(RivalType.CAMPAIGN_WAR.name())) {
            abstractRivalService = rivalCampaignWarService;
            rivalType = RivalType.CAMPAIGN_WAR;
        } else if (message.startsWith(RivalType.CHALLENGE.name())) {
            abstractRivalService = rivalChallengeService;
            rivalType = RivalType.CHALLENGE;
        }
        if (message.contains(ANSWER_SUFFIX)) {
            abstractRivalService.answer(session.getId(), trimPrefixFromMessage(message, rivalType, ANSWER_SUFFIX));
        } else if (message.contains(CHOOSE_TASK_PROPS_SUFFIX)) {
            abstractRivalService.chooseTaskProps(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_TASK_PROPS_SUFFIX));
        } else if (message.contains(SURRENDER_SUFFIX)) {
            abstractRivalService.surrender(session.getId());
        } else if (message.contains(CHOOSE_WHO_ANSWER_SUFFIX)) {
            abstractRivalService.chooseWhoAnswer(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_WHO_ANSWER_SUFFIX));
        }
    }

    private String trimPrefixFromMessage(String message, RivalType rivalType, String messageSuffix) {
        return message.substring(rivalType.name().length() + messageSuffix.length());
    }
}