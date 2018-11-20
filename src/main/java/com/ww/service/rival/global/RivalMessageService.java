package com.ww.service.rival.global;

import com.ww.game.play.PlayManager;
import com.ww.model.container.Connection;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ww.helper.ModelHelper.parseMessage;

@Service
@AllArgsConstructor
public class RivalMessageService {
    private static Logger logger = LoggerFactory.getLogger(RivalMessageService.class);

    public static final String ANSWER = "ANSWER";
    public static final String CHOOSE_TASK_CATEGORY = "CHOOSE_TASK_CATEGORY";
    public static final String CHOOSE_TASK_DIFFICULTY = "CHOOSE_TASK_DIFFICULTY";
    public static final String SURRENDER = "SURRENDER";
    public static final String CHOOSE_WHO_ANSWER = "CHOOSE_WHO_ANSWER";
    public static final String HINT = "HINT";
    public static final String WATER_PISTOL = "WATER_PISTOL";
    public static final String LIFEBUOY = "LIFEBUOY";
    public static final String NINJA = "NINJA";
    public static final String GHOST = "GHOST";
    public static final String PIZZA = "PIZZA";
    public static final String COVERALL = "COVERALL";
    public static final String CHANGE_TASK = "CHANGE_TASK";

    private final ConnectionService connectionService;
    private final RivalGlobalService rivalGlobalService;



    public void handleMessage(String sessionId, String message) {
        logger.trace("Message received sessionId: {}, content: {}", sessionId, message);
        Optional<Connection> optionalConnection = connectionService.findBySessionId(sessionId);
        if (!optionalConnection.isPresent()) {
            return;
        }
        Long profileId = optionalConnection.get().getProfileId();
        if (!rivalGlobalService.contains(profileId)) {
            return;
        }
        PlayManager manager = rivalGlobalService.get(profileId);
        manager.processMessage(profileId, parseMessage(message));
    }
}
