package com.ww.service.rival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRivalService {
    protected abstract void addRewardFromWin(Profile winner);

    public abstract Message getMessageContent();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract TaskGenerateService getTaskGenerateService();

    protected abstract TaskRendererService getTaskRendererService();

    public abstract ProfileService getProfileService();

    public abstract RivalGlobalService getRivalGlobalService();

    public synchronized void disposeManager(RivalManager rivalManager) {
        if (!rivalManager.isClosed()) {
            return;
        }
        rivalManager.getRivalProfileContainers().forEach(rivalProfileContainer -> {
            getRivalGlobalService().remove(rivalProfileContainer.getProfileId());
        });
        RivalContainer rivalContainer = rivalManager.getContainer();
        Boolean isDraw = rivalContainer.getDraw();
        Profile winner = rivalContainer.getWinner();
        Rival rival = new Rival(rivalContainer.getType(), rivalContainer.getImportance(), rivalContainer.getCreatorProfile(), rivalContainer.getOpponentProfile(), isDraw, winner);
        if (!isDraw) {
            addRewardFromWin(winner);
        }
        getRivalGlobalService().save(rival);
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
        RivalManager rivalManager = getRivalGlobalService().get(profileId.get());
        if (!rivalManager.canAnswer()) {
            return;
        }
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            rivalManager.answer(profileId.get(), contentMap);
        }
    }

    public synchronized void hint(String sessionId, String content) {
    }

    public synchronized void chooseTaskProps(String sessionId, String content) {
        Optional<Long> profileId = getProfileConnectionService().getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        RivalManager rivalManager = getRivalGlobalService().get(profileId.get());
        if (!rivalManager.canChooseTaskProps()) {
            return;
        }
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            rivalManager.chosenTaskProps(profileId.get(), contentMap);
        }
    }

    public synchronized void chooseWhoAnswer(String sessionId, String content) {
    }

    public synchronized void surrender(String sessionId) {
        Optional<Long> optionalProfileId = getProfileConnectionService().getProfileId(sessionId);
        if (!optionalProfileId.isPresent()) {
            return;
        }
        Long profileId = optionalProfileId.get();
        if (!getRivalGlobalService().contains(profileId)) {
            return;
        }
        RivalManager rivalManager = getRivalGlobalService().get(profileId);
        rivalManager.surrender(profileId);
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel) {
        return getTaskGenerateService().generate(category, difficultyLevel);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return getTaskRendererService().prepareTaskDTO(question);
    }

}
