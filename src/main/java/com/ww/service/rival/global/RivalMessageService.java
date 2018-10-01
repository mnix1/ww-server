package com.ww.service.rival.global;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.repository.outside.rival.RivalRepository;
import com.ww.service.rival.RivalChallengeService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RivalMessageService {
    private static Logger logger = LoggerFactory.getLogger(RivalMessageService.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private RivalMessageService rivalMessageService;

    @Autowired
    RivalGlobalService rivalGlobalService;

    @Autowired
    RivalCampaignWarService rivalCampaignWarService;

    @Autowired
    RivalChallengeService rivalChallengeService;

    public void handleMessage(String sessionId, String message){
        logger.trace("Message received sessionId: {}, content: {}", sessionId,message );
        Optional<ProfileConnection> optionalProfileConnection = profileConnectionService.findBySessionId(sessionId);
        if (!optionalProfileConnection.isPresent() || !rivalGlobalService.contains(optionalProfileConnection.get().getProfileId())) {
            return;
        }
        RivalManager rivalManager = rivalGlobalService.get(optionalProfileConnection.get().getProfileId());
        rivalManager.processMessage(optionalProfileConnection.get().getProfileId(), message);

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
//        if (message.contains(ANSWER_SUFFIX)) {
//            abstractRivalService.answer(session.getId(), trimPrefixFromMessage(message, rivalType, ANSWER_SUFFIX));
//        } else if (message.contains(CHOOSE_TASK_PROPS_SUFFIX)) {
//            abstractRivalService.chooseTaskProps(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_TASK_PROPS_SUFFIX));
//        } else if (message.contains(SURRENDER_SUFFIX)) {
//            abstractRivalService.surrender(session.getId());
//        } else if (message.contains(CHOOSE_WHO_ANSWER_SUFFIX)) {
//            abstractRivalService.chooseWhoAnswer(session.getId(), trimPrefixFromMessage(message, rivalType, CHOOSE_WHO_ANSWER_SUFFIX));
//        } else if (message.contains(HINT_SUFFIX)) {
//            abstractRivalService.hint(session.getId(), trimPrefixFromMessage(message, rivalType, HINT_SUFFIX));
//        }
    }

    private static final String ANSWER_SUFFIX = "_^_ANSWER";
    private static final String CHOOSE_TASK_PROPS_SUFFIX = "_^_CHOOSE_TASK_PROPS";
    private static final String SURRENDER_SUFFIX = "_^_SURRENDER";
    private static final String CHOOSE_WHO_ANSWER_SUFFIX = "_^_CHOOSE_WHO_ANSWER";
    private static final String HINT_SUFFIX = "_^_HINT";

    private String trimPrefixFromMessage(String message, RivalType rivalType, String messageSuffix) {
        return message.substring(rivalType.name().length() + messageSuffix.length());
    }
}
