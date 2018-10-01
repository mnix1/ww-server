package com.ww.service.rival.campaign;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.init.RivalRunService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.wisie.WisieService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.manager.rival.campaign.CampaignWarManager.BOT_PROFILE_ID;
import static com.ww.model.constant.rival.RivalType.CAMPAIGN_WAR;

@Service
public class RivalCampaignWarService extends RivalWarService {
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

    @Autowired
    protected CampaignService campaignService;

    @Autowired
    protected RivalRunService rivalRunService;

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
        return Message.CAMPAIGN_WAR_CONTENT;
    }

    @Override
    public void disposeManager(RivalManager rivalManager) {
        super.disposeManager(rivalManager);
        CampaignWarManager campaignWarManager = (CampaignWarManager) rivalManager;
        Long profileId = rivalManager.getRivalContainer().getCreatorProfile().getId();
        ProfileCampaign profileCampaign = campaignService.active(profileId);
        campaignService.setProfileCampaignWisies(profileCampaign);
        if (profileCampaign.getProfile().equals(rivalManager.getRivalContainer().getWinner())) {
            profileCampaign.setPhase(profileCampaign.getPhase() + 1);
            profileCampaign.updateResourceGains();
            if (profileCampaign.getPhase() >= profileCampaign.getCampaign().getPhases()) {
                profileCampaign.setStatus(ProfileCampaignStatus.FINISHED);
                profileCampaign.setBookGain(campaignService.getBookGainForCampaign(profileCampaign.getCampaign()));
            }
            List<TeamMember> teamMembers = campaignWarManager.warContainer.getRivalProfileContainer(profileId).getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                if (teamMember.isWisie()) {
                    for (ProfileCampaignWisie wisie : profileCampaign.getWisies()) {
                        if (wisie.equals(teamMember.getContent())) {
                            wisie.setDisabled(!teamMember.isPresent());
                        }
                    }
                } else {
                    profileCampaign.setPresent(teamMember.isPresent());
                }
            }
        } else {
            profileCampaign.setStatus(ProfileCampaignStatus.FINISHED);
            for (ProfileCampaignWisie wisie : profileCampaign.getWisies()) {
                wisie.setDisabled(true);
            }
            profileCampaign.setPresent(false);
        }
        campaignService.save(profileCampaign);
    }

    @Override
    protected void addRewardFromWin(Profile winner) {
    }

    public Map<String, Object> start() {
        Map<String, Object> model = new HashMap<>();
        ProfileCampaign profileCampaign = campaignService.active();
        if (profileCampaign == null) {
            return putErrorCode(model);
        }
        RivalTwoPlayerInitContainer rival = new RivalTwoPlayerInitContainer(CAMPAIGN_WAR, RivalImportance.FAST, profileService.getProfile(), prepareComputerProfile(profileCampaign));
        rivalRunService.run(rival);
        return putSuccessCode(model);
    }

    public Profile prepareComputerProfile(ProfileCampaign profileCampaign) {
        Profile computerProfile = new Profile();
        computerProfile.setId(BOT_PROFILE_ID);
        computerProfile.setName("");
        computerProfile.setWisorType(WisorType.random());
        computerProfile.setTag("0");
        Set<WisieType> wisieTypes = new HashSet<>();
        while (wisieTypes.size() < (Math.min(profileCampaign.getPhase() + 1, 5))) {
            wisieTypes.add(WisieType.random());
        }
        long profileWisieId = -1;
        for (WisieType wisieType : wisieTypes) {
            ProfileWisie profileWisie = new ProfileWisie(computerProfile, wisieService.getWisie(wisieType));
            profileWisie.setId(profileWisieId--);
            profileWisieService.initWisieAttributes(profileWisie);
            profileWisieService.initWisieHobbies(profileWisie);
            int promo = 10 * profileCampaign.getPhase() * profileCampaign.getCampaign().getDifficultyLevel().getRating();
            for (MentalAttribute attribute : MentalAttribute.values()) {
                profileWisie.setMentalAttributeValue(attribute, Math.pow(profileWisie.getMentalAttributeValue(attribute), 1.1) + promo);
            }
            for (WisdomAttribute attribute : WisdomAttribute.values()) {
                profileWisie.setWisdomAttributeValue(attribute, Math.pow(profileWisie.getWisdomAttributeValue(attribute), 1.1) + promo);
            }
            computerProfile.getWisies().add(profileWisie);
        }
        return computerProfile;
    }

    private RivalManager createManager(RivalInitContainer rival, ProfileCampaign profileCampaign) {
        return new CampaignWarManager(rival, this, profileConnectionService, profileCampaign);
    }

}
