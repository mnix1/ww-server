package com.ww.service.rival.war;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.RivalService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

@Service
@Getter
public class RivalWarService extends RivalService {

    @Autowired
    private ProfileWisieService profileWisieService;

    @Autowired
    private TaskService taskService;

    @Override
    public void addRewardFromWin(Profile profile) {
        getRewardService().addSendRewardFromRivalWin(profile, new Resources(2L));
    }

    public List<ProfileWisie> getProfileWisies(Profile profile) {
        return profileWisieService.findAllInTeam(profile.getId());
    }

    @Override
    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        Question question = super.prepareQuestion(category, difficultyLevel, language);
        initTaskWisdomAttributes(question);
        return question;
    }

    public void initTaskWisdomAttributes(Question question) {
        taskService.initTaskWisdomAttributes(question);
    }

    public Profile prepareComputerProfile() {
        Profile computerProfile = new Profile();
        computerProfile.setId(BOT_PROFILE_ID);
        computerProfile.setName("");
        computerProfile.setWisorType(WisorType.random());
        computerProfile.setTag("0");
        return computerProfile;
    }
}
