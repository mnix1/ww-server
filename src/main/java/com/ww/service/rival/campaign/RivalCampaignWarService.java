package com.ww.service.rival.campaign;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.wisie.WisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ww.manager.rival.campaign.CampaignWarManager.BOT_PROFILE_ID;
import static com.ww.model.constant.rival.RivalType.CAMPAIGN_WAR;

@Service
public class RivalCampaignWarService extends RivalWarService {
    @Autowired
    private WisieService wisieService;

    @Autowired
    private CampaignService campaignService;

    @Override
    public void disposeManager(RivalManager manager) {
        super.disposeManager(manager);
        CampaignWarManager campaignWarManager = (CampaignWarManager) manager;
        Long profileId = manager.getModel().getCreatorProfile().getId();
        ProfileCampaign profileCampaign = campaignService.active(profileId).orElseThrow(IllegalArgumentException::new);
        campaignService.setProfileCampaignWisies(profileCampaign);
        if (profileCampaign.getProfile().equals(manager.getModel().getWinner())) {
            profileCampaign.setPhase(profileCampaign.getPhase() + 1);
            profileCampaign.updateResourceGains();
            if (profileCampaign.getPhase() >= profileCampaign.getCampaign().getPhases()) {
                profileCampaign.setStatus(ProfileCampaignStatus.FINISHED);
                profileCampaign.setBookGain(campaignService.getBookGainForCampaign(profileCampaign.getCampaign()));
            }
            List<TeamMember> teamMembers = ((WarTeam) campaignWarManager.getTeam(profileId)).getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                if (teamMember.isWisie()) {
                    for (ProfileCampaignWisie wisie : profileCampaign.getWisies()) {
                        if (wisie.equals(((WarWisie) teamMember.getContent()).getWisie())) {
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
    public void addRewardFromWin(Profile winner) {
    }

    public Optional<RivalCampaignWarInit> init() {
        Optional<ProfileCampaign> optionalProfileCampaign = campaignService.active();
        return optionalProfileCampaign.map(profileCampaign -> new RivalCampaignWarInit(CAMPAIGN_WAR, RivalImportance.FAST, getProfileService().getProfile(), prepareComputerProfile(profileCampaign), profileCampaign));
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
            getProfileWisieService().initWisieAttributes(profileWisie);
            getProfileWisieService().initWisieHobbies(profileWisie);
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

}
