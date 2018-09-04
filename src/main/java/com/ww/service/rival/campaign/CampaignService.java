package com.ww.service.rival.campaign;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.campaign.CampaignManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.RivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.war.WarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.wisie.WisieService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ww.model.constant.rival.RivalType.CAMPAIGN;

@Service
public class CampaignService extends WarService {

    private static final Long botProfileId = -1L;
    @Autowired
    protected ProfileConnectionService profileConnectionService;

    @Autowired
    protected TaskGenerateService taskGenerateService;

    @Autowired
    protected TaskRendererService taskRendererService;

    @Autowired
    protected RewardService rewardService;

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected ProfileWisieService profileWisieService;

    @Autowired
    protected WisieService wisieService;

    @Override
    protected void addRewardFromWin(Profile winner) {
//        rewardService.addRewardFromBattleWin(winner);
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
        return Message.CAMPAIGN_CONTENT;
    }

    public Map<String, Object> start() {
        Map<String, Object> model = new HashMap<>();
        RivalInitContainer rival = new RivalInitContainer(CAMPAIGN, RivalImportance.FAST, profileService.getProfile(), prepareComputerProfile());
        RivalManager rivalManager = createManager(rival);
        getProfileIdToRivalManagerMap().put(rival.getCreatorProfile().getId(), rivalManager);
        getProfileIdToRivalManagerMap().put(rival.getOpponentProfile().getId(), rivalManager);
        rivalManager.maybeStart(rival.getOpponentProfile().getId());
        model.put("code", 1);
        return model;
    }

    public Profile prepareComputerProfile() {
        Profile computerProfile = new Profile();
        computerProfile.setId(botProfileId);
        computerProfile.setName("Wielki Chrabąszcz");
        computerProfile.setWisorType("wisor18");
        computerProfile.setTag("0");
        computerProfile.setTeamInitialized(true);
        List<WisieType> wisieTypes = Arrays.asList(WisieType.BEAR, WisieType.CROCODILE, WisieType.CAT_TEACHER, WisieType.BEE);
        long profileWisieId = -1;
        for (WisieType wisieType : wisieTypes) {
            ProfileWisie profileWisie = new ProfileWisie(computerProfile, wisieService.getWisie(wisieType));
            profileWisie.setId(profileWisieId--);
            profileWisieService.initWisieAttributes(profileWisie);
            profileWisieService.initWisieHobbies(profileWisie);
            computerProfile.getWisies().add(profileWisie);
        }
        return computerProfile;
    }

    private RivalManager createManager(RivalInitContainer rival) {
        return new CampaignManager(rival, this, profileConnectionService);
    }

    public List<ProfileWisie> getProfileWisies(Profile profile) {
        if (profile.getId().equals(botProfileId)) {
            return new ArrayList<>(profile.getWisies());
        }
        return profileWisieService.listTeam(profile.getId());
    }


}
