package com.ww.service.rival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.BattleManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleInitContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BattleService {
    private static final Logger logger = LoggerFactory.getLogger(BattleService.class);

    private final CopyOnWriteArrayList<BattleInitContainer> battleInitContainers = new CopyOnWriteArrayList<BattleInitContainer>();
    private ConcurrentHashMap<String, BattleManager> battleManagers = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<Profile> waitingForBattleProfiles = new CopyOnWriteArrayList<>();

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

    public Map startFriend(String tag) {
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

    public Map startFast() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        if (!waitingForBattleProfiles.contains(profile)) {
            waitingForBattleProfiles.add(profile);
        }
        model.put("code", 1);
        return model;
    }

    public Map cancelFast() {
        Map<String, Object> model = new HashMap<>();
        waitingForBattleProfiles.removeIf(profile -> profile.getId().equals(sessionService.getProfileId()));
        return model;
    }

    @Scheduled(fixedRate = 5000)
    private void maybeInitFastBattle() {
        if (waitingForBattleProfiles.isEmpty()) {
//            logger.debug("No waiting for battle profiles");
            return;
        }
        if (waitingForBattleProfiles.size() == 1) {
//            logger.debug("Only one waiting for battle profile");
            return;
        }
        Profile profile = waitingForBattleProfiles.get(0);
        Optional<ProfileConnection> profileConnection = profileConnectionService.findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForBattleProfiles.removeIf(e -> e.getId().equals(profile.getId()));
            maybeInitFastBattle();
            return;
        }
        Profile opponent = findOpponentForFastBattle(profile);
        Optional<ProfileConnection> opponentConnection = profileConnectionService.findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForBattleProfiles.removeIf(e -> e.getId().equals(opponent.getId()));
            maybeInitFastBattle();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating battle manager", profile.getId(), opponent.getId());
        waitingForBattleProfiles.remove(profile);
        waitingForBattleProfiles.remove(opponent);
        BattleInitContainer battle = new BattleInitContainer(profile, profileConnection.get(), opponent, opponentConnection.get());
        BattleManager battleManager = new BattleManager(battle, this);
        battleManagers.put(battle.getCreatorProfileConnection().getSessionId(), battleManager);
        battleManagers.put(battle.getOpponentProfileConnection().getSessionId(), battleManager);
        battleManager.startFast();
        return;
    }

    private Profile findOpponentForFastBattle(Profile profile) {
        // TODO add more logic
        for (Profile p : waitingForBattleProfiles) {
            if (!p.getId().equals(profile.getId())) {
                return p;
            }
        }
        return null;
    }

    private void cleanBattlesCreator() {
        battleInitContainers.removeIf(battleInitContainer -> battleInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    private void cleanBattlesOpponent() {
        battleInitContainers.removeIf(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancelFriend() {
        Map<String, Object> model = new HashMap<>();
        battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanBattlesCreator();
        return model;
    }

    public Map acceptFriend() {
        Map<String, Object> model = new HashMap<>();
        BattleInitContainer battle = battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId())).findFirst().get();
        BattleManager battleManager = new BattleManager(battle, this);
        battleManagers.put(battle.getCreatorProfileConnection().getSessionId(), battleManager);
        battleManagers.put(battle.getOpponentProfileConnection().getSessionId(), battleManager);
        this.sendAcceptInvite(battle);
        return model;
    }

    public Map rejectFriend() {
        Map<String, Object> model = new HashMap<>();
        battleInitContainers.stream().filter(battleInitContainer -> battleInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanBattlesOpponent();
        return model;
    }

    private BattleInitContainer prepareBattleContainer(String tag) {
        Profile opponentProfile = profileService.getProfile(tag);
        ProfileConnection opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
        if (opponentProfileConnection == null) {
            logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
            return null;
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

    public synchronized void readyForStart(String sessionId) {
        BattleManager battleManager = battleManagers.get(sessionId);
        battleManager.maybeStart(sessionId);
    }

    public synchronized void answer(String sessionId, String content) {
        BattleManager battleManager = battleManagers.get(sessionId);
        if (battleManager.isLock()) {
            return;
        }
        battleManager.setLock(true);
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

    public TaskDTO prepareQuestionDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }


}
