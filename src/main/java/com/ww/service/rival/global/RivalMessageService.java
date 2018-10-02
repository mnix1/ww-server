package com.ww.service.rival.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RivalMessageService {
    private static Logger logger = LoggerFactory.getLogger(RivalMessageService.class);

    public static final String ANSWER = "ANSWER";
    public static final String CHOOSE_TASK_PROPS = "CHOOSE_TASK_PROPS";
    public static final String SURRENDER = "SURRENDER";
    public static final String CHOOSE_WHO_ANSWER = "CHOOSE_WHO_ANSWER";
    public static final String HINT = "HINT";

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private RivalGlobalService rivalGlobalService;

    public synchronized Map<String, Object> handleInput(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public void handleMessage(String sessionId, String message) {
        logger.trace("Message received sessionId: {}, content: {}", sessionId, message);
        Optional<ProfileConnection> optionalProfileConnection = profileConnectionService.findBySessionId(sessionId);
        if (!optionalProfileConnection.isPresent() || !rivalGlobalService.contains(optionalProfileConnection.get().getProfileId())) {
            return;
        }
        RivalManager rivalManager = rivalGlobalService.get(optionalProfileConnection.get().getProfileId());
        rivalManager.getFlow().processMessage(optionalProfileConnection.get().getProfileId(), handleInput(message));

//        AbstractRivalService abstractRivalService = null;
//        RivalType rivalType = null;
//        if (message.startsWith(RivalType.BATTLE.name())) {
//            abstractRivalService = rivalBattleService;
//            rivalType = RivalType.BATTLE;
//        } else if (message.startsWith(RivalType.WAR.name())) {
//            abstractRivalService = rivalWarService;
//            rivalType = RivalType.WAR;
//        } else if (message.startsWith(RivalType.CAMPAIGN_WAR.name())) {
//            abstractRivalService = rivalCampaignWarService;
//            rivalType = RivalType.CAMPAIGN_WAR;
//        } else if (message.startsWith(RivalType.CHALLENGE.name())) {
//            abstractRivalService = rivalChallengeService;
//            rivalType = RivalType.CHALLENGE;
//        }
//        if (message.contains(ANSWER)) {
//            abstractRivalService.answer(session.getId(), trimPrefixFromMessage(message, rivalType, ANSWER));
//        } else if (message.contains(CHOOSE_TASK_PROPS)) {
//            abstractRivalService.chooseTaskProps(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_TASK_PROPS));
//        } else if (message.contains(SURRENDER)) {
//            abstractRivalService.surrender(session.getId());
//        } else if (message.contains(CHOOSE_WHO_ANSWER)) {
//            abstractRivalService.chooseWhoAnswer(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_WHO_ANSWER));
//        } else if (message.contains(HINT)) {
//            abstractRivalService.hint(session.getId(), trimPrefixFromMessage(message, rivalType, HINT));
//        }
    }

    private String trimPrefixFromMessage(String message, RivalType rivalType, String messageSuffix) {
        return message.substring(rivalType.name().length() + messageSuffix.length());
    }
}
