package com.ww.service.rival.war;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.AbstractRivalService;
import com.ww.service.rival.GlobalRivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RivalWarService extends AbstractRivalService {

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

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected GlobalRivalService globalRivalService;

    @Override
    public GlobalRivalService getGlobalRivalService() {
        return globalRivalService;
    }

    @Override
    protected void addRewardFromWin(Profile profile) {
        rewardService.addRewardFromWarWin(profile);
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
    public ProfileService getProfileService() {
        return profileService;
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

    public List<ProfileWisie> getProfileWisies(Profile profile) {
        return profileWisieService.listTeam(profile.getId());
    }

    @Override
    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel) {
        Question question = super.prepareQuestion(category, difficultyLevel);
        initTaskWisdomAttributes(question);
        return question;
    }

    public void initTaskWisdomAttributes(Question question) {
        taskService.initTaskWisdomAttributes(question);
    }

    public synchronized void chooseWhoAnswer(String sessionId, String content) {
        Optional<Long> profileId = getProfileConnectionService().getProfileId(sessionId);
        if (!profileId.isPresent()) {
            return;
        }
        WarManager warManager = (WarManager) getGlobalRivalService().get(profileId.get());
        if (!warManager.canChooseWhoAnswer()) {
            return;
        }
        Map<String, Object> contentMap = handleInput(content);
        if (contentMap != null) {
            warManager.chosenWhoAnswer(profileId.get(), contentMap);
        }
    }
}
