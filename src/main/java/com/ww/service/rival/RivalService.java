package com.ww.service.rival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivalService {
    protected final ConcurrentHashMap<Long, RivalManager> profileIdToRivalManagerMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, RivalManager> getProfileIdToRivalManagerMap() {
        return profileIdToRivalManagerMap;
    }

    public synchronized void sendActualRivalModelToNewProfileConnection(ProfileConnection profileConnection) {
        if (!profileIdToRivalManagerMap.containsKey(profileConnection.getProfileId())) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileConnection.getProfileId());
        rivalManager.send(rivalManager.actualModel(profileConnection.getProfileId()), getMessageContent(), profileConnection.getProfileId());
    }

    public synchronized void readyForStart(String sessionId) {
        getProfileConnectionService().getProfileId(sessionId).ifPresent(profileId -> {
            RivalManager rivalManager = profileIdToRivalManagerMap.get(profileId);
            rivalManager.maybeStart(profileId);
        });
    }

    protected abstract void addRewardFromWin(String winnerTag);

    protected abstract Message getMessageContent();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract TaskGenerateService getTaskGenerateService();

    protected abstract TaskRendererService getTaskRendererService();

    public synchronized void disposeManager(RivalManager rivalManager) {
        if (!rivalManager.isClosed()) {
            return;
        }
        String winnerTag = rivalManager.getWinnerTag();
        List<RivalProfileContainer> rivalProfileContainers = rivalManager.getRivalProfileContainers();
        rivalProfileContainers.forEach(rivalProfileContainer -> {
            if (profileIdToRivalManagerMap.containsKey(rivalProfileContainer.getProfileId())) {
                profileIdToRivalManagerMap.remove(rivalProfileContainer.getProfileId());
            }
        });
        if (winnerTag != null) {
            addRewardFromWin(winnerTag);
        }

        // TODO STORE RESULT
    }

    public synchronized void answer(String sessionId, String content) {
        Optional<Long> profileId = getProfileConnectionService().getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileId.get());
        if (!rivalManager.canAnswer()) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(content, HashMap.class);
            rivalManager.stateAnswered(profileId.get(), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void chooseTaskProps(String sessionId, String content) {
        Optional<Long> profileId = getProfileConnectionService().getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileId.get());
        if (!rivalManager.canChooseTaskProps()) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(content, HashMap.class);
            rivalManager.stateChosenTaskProps(profileId.get(), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void surrender(String sessionId) {
        Optional<Long> optionalProfileId = getProfileConnectionService().getProfileId(sessionId);
        if (!optionalProfileId.isPresent()) {
            return;
        }
        Long profileId = optionalProfileId.get();
        if (!profileIdToRivalManagerMap.containsKey(profileId)) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileId);
        rivalManager.surrender(profileId);
    }

    public Question prepareQuestion(Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = getTaskGenerateService().generate(category, difficultyLevel);
        question.initAnswerIds();
        return question;
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return getTaskRendererService().prepareTaskDTO(question);
    }

}
