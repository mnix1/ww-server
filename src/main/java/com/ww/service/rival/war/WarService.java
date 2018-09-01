package com.ww.service.rival.war;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.rival.RivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.RewardService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WarService extends RivalService {

    @Autowired
    protected ProfileWisieService profileWisieService;

    @Autowired
    protected ProfileConnectionService profileConnectionService;

    @Autowired
    protected TaskGenerateService taskGenerateService;

    @Autowired
    protected TaskRendererService taskRendererService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RewardService rewardService;

    @Override
    protected void addRewardFromWin(String winnerTag) {
        rewardService.addRewardFromWarWin(winnerTag);
    }

    @Override
    protected ProfileConnectionService getProfileConnectionService() {
        return profileConnectionService;
    }

    @Override
    protected TaskGenerateService getTaskGenerateService() {
        return taskGenerateService;
    }

    @Override
    protected TaskRendererService getTaskRendererService() {
        return taskRendererService;
    }

    @Override
    protected Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

    public List<ProfileWisie> getProfileWisies(Long profileId) {
        return profileWisieService.listTeam(profileId);
    }

    public Question prepareQuestion(Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = super.prepareQuestion(category, difficultyLevel);
        taskService.initTaskWisdomAtributes(question);
        return question;
    }


    public synchronized void chooseWhoAnswer(String sessionId, String content) {
        Optional<Long> profileId = getProfileConnectionService().getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        WarManager warManager = (WarManager) profileIdToRivalManagerMap.get(profileId.get());
        if (!warManager.canChooseWhoAnswer()) {
            return;
        }
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            warManager.chosenWhoAnswer(profileId.get(), contentMap);
        }
    }
}
