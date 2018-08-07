package com.ww.service.rival.battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.BattleManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.shop.ShopService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BattleService {
    private static final Logger logger = LoggerFactory.getLogger(BattleService.class);

    private final ConcurrentHashMap<Long, BattleManager> profileIdToBattleManagerMap = new ConcurrentHashMap<>();

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private TaskGenerateService taskGenerateService;

    @Autowired
    private TaskRendererService taskRendererService;

    public ConcurrentHashMap<Long, BattleManager> getProfileIdToBattleManagerMap() {
        return profileIdToBattleManagerMap;
    }

    public synchronized void sendActualBattleModelToNewProfileConnection(ProfileConnection profileConnection) {
        if (!profileIdToBattleManagerMap.containsKey(profileConnection.getProfileId())) {
            return;
        }
        BattleManager battleManager = profileIdToBattleManagerMap.get(profileConnection.getProfileId());
        battleManager.send(battleManager.actualModel(profileConnection.getProfileId()), Message.BATTLE_ACTUAL_MODEL, profileConnection.getProfileId());
    }

    public synchronized void readyForStart(String sessionId) {
        profileConnectionService.getProfileId(sessionId).ifPresent(profileId -> {
            BattleManager battleManager = profileIdToBattleManagerMap.get(profileId);
            battleManager.maybeStart(profileId);
        });
    }

    public synchronized void disposeManager(BattleManager battleManager) {
        if (!battleManager.isClosed()) {
            return;
        }
        String winnerTag = battleManager.getWinnerTag();
        List<BattleProfileContainer> battleProfileContainers = battleManager.getBattleProfileContainers();
        battleProfileContainers.forEach(battleProfileContainer -> {
            if (profileIdToBattleManagerMap.containsKey(battleProfileContainer.getProfileId())) {
                profileIdToBattleManagerMap.remove(battleProfileContainer.getProfileId());
            }
        });
        shopService.addChest(winnerTag);
        // TODO STORE RESULT
    }

    public synchronized void answer(String sessionId, String content) {
        Optional<Long> profileId = profileConnectionService.getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        BattleManager battleManager = profileIdToBattleManagerMap.get(profileId.get());
        if (battleManager.isLock()) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(content, HashMap.class);
            battleManager.answer(profileId.get(), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Question prepareQuestion() {
        Question question = taskGenerateService.generate(Category.random(), TaskDifficultyLevel.random());
        question.initAnswerIds();
        return question;
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }


}
