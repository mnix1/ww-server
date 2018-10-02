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
    public static final String WATER_PISTOL = "WATER_PISTOL";
    public static final String LIFEBUOY = "LIFEBUOY";

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
        RivalManager manager = rivalGlobalService.get(optionalProfileConnection.get().getProfileId());
        manager.getFlow().processMessage(optionalProfileConnection.get().getProfileId(), handleInput(message));
    }
}
