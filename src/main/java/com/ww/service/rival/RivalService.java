package com.ww.service.rival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
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

    protected abstract void addRewardFromWin(Profile winner);

    protected abstract void rankingGameResult(Boolean isDraw, Profile winner, Profile looser);

    protected abstract Message getMessageContent();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract TaskGenerateService getTaskGenerateService();

    protected abstract TaskRendererService getTaskRendererService();

    public synchronized void disposeManager(RivalManager rivalManager) {
        if (!rivalManager.isClosed()) {
            return;
        }
        List<RivalProfileContainer> rivalProfileContainers = rivalManager.getRivalProfileContainers();
        rivalProfileContainers.forEach(rivalProfileContainer -> {
            if (profileIdToRivalManagerMap.containsKey(rivalProfileContainer.getProfileId())) {
                profileIdToRivalManagerMap.remove(rivalProfileContainer.getProfileId());
            }
        });
        RivalContainer rivalContainer = rivalManager.getRivalContainer();
        Boolean isDraw = rivalContainer.getIsDraw();
        Profile winner = rivalContainer.getWinner();
        if (!isDraw) {
            addRewardFromWin(winner);
        }
        if (rivalManager.isRanking()) {
            Profile looser = rivalContainer.getLooser();
            rankingGameResult( isDraw, winner, looser);
        }
        // TODO STORE RESULT
    }

    public synchronized Map<String, Object> handleInput(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            rivalManager.answer(profileId.get(), contentMap);
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
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            rivalManager.chosenTaskProps(profileId.get(), contentMap);
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
