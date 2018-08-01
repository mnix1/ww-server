package com.ww.service.rival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.BattleManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleInitContainer;
import com.ww.model.dto.rival.task.QuestionDTO;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BattleService {
    private static final Logger logger = LoggerFactory.getLogger(BattleService.class);

    private final CopyOnWriteArrayList<BattleInitContainer> battleInitContainers = new CopyOnWriteArrayList<BattleInitContainer>();
    private ConcurrentHashMap<String, BattleManager> battleManagers = new ConcurrentHashMap<>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private TaskGenerateService taskGenerateService;

    @Autowired
    private TaskRendererService taskRendererService;

    public Map start(String tag) {
        Map<String, Object> model = new HashMap<>();
        cleanBattlesCreator();
        cleanBattlesOpponent();
        BattleInitContainer battleInitContainer = prepareBattleContainer(tag);
        if (battleInitContainer == null) {
            model.put("code", -1);
            return model;
        }
        battleInitContainers.add(battleInitContainer);
        model.put("code", 1);
        return model;
    }

    private void cleanBattlesCreator() {
        battleInitContainers.removeIf(battleInitContainer -> battleInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    private void cleanBattlesOpponent() {
        battleInitContainers.removeIf(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancel() {
        Map<String, Object> model = new HashMap<>();
        battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanBattlesCreator();
        return model;
    }

    public Map accept() {
        Map<String, Object> model = new HashMap<>();
        BattleInitContainer battle = battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId())).findFirst().get();
        BattleManager battleManager = new BattleManager(battle, this);
        battleManagers.put(battle.getCreatorProfileConnection().getSessionId(), battleManager);
        battleManagers.put(battle.getOpponentProfileConnection().getSessionId(), battleManager);
        this.sendAcceptInvite(battle);
        return model;
    }

    public Map reject() {
        Map<String, Object> model = new HashMap<>();
        battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanBattlesOpponent();
        return model;
    }

    private BattleInitContainer prepareBattleContainer(String tag) {
        ProfileConnection opponentProfileConnection = null;
        Profile opponentProfile = null;
        if (tag == null) {
            //TODO autobinding profiles
//            List<ProfileConnection> profileConnections = profileConnectionService.getProfileConnections();
//            if (profileConnections.isEmpty()) {
//                //TODO add bot because no users
//                logger.error("No connected profiles: {}", sessionService.getProfileId());
//                return null;
//            }
//            //TODO add logic to find closest skill opponent
//            opponentProfileConnection = profileConnections.get(0);
//            opponentProfile = profileService.getProfile(opponentProfileConnection.getProfileId());
        } else {
            opponentProfile = profileService.getProfile(tag);
            opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
            if (opponentProfileConnection == null) {
                logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
                return null;
            }
        }
        Profile creatorProfile = profileService.getProfile();
        ProfileConnection creatorProfileConnection = profileConnectionService.findByProfileId();
        BattleInitContainer battle = new BattleInitContainer(creatorProfile, creatorProfileConnection, opponentProfile, opponentProfileConnection);
        sendInvite(battle);
        return battle;
    }

    private void sendInvite(BattleInitContainer battleInitContainer) {
        battleInitContainer.getOpponentProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_INVITE, new FriendDTO(battleInitContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true).toString()).toString());
    }

    private void sendCancelInvite(BattleInitContainer battleInitContainer) {
        battleInitContainer.getOpponentProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_CANCEL_INVITE, "").toString());
    }

    private void sendRejectInvite(BattleInitContainer battleInitContainer) {
        battleInitContainer.getCreatorProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_REJECT_INVITE, "").toString());
    }

    private void sendAcceptInvite(BattleInitContainer battleInitContainer) {
        battleInitContainer.getCreatorProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_ACCEPT_INVITE, "").toString());
    }

    public void readyForStart(String sessionId) {
        BattleManager battleManager = battleManagers.get(sessionId);
        battleManager.maybeStart(sessionId);
    }

    public void answer(String sessionId, String content) {
        BattleManager battleManager = battleManagers.get(sessionId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(content, HashMap.class);
            battleManager.answer(sessionId, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Question prepareQuestion() {
        Question question = taskGenerateService.generate(Category.random());
        question.initAnswerIds();
        return question;
    }

    public QuestionDTO prepareQuestionDTO(Question question) {
        return taskRendererService.prepareQuestionDTO(question);
    }


}
