package com.ww.service.rival.campaign;

import com.ww.game.play.PlayCampaignManager;
import com.ww.game.play.PlayManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.RivalWisieComputerService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ConnectionService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ww.model.constant.rival.RivalType.CAMPAIGN_WAR;

@Service
public class RivalCampaignWarService extends RivalWisieComputerService {

    private final CampaignService campaignService;

    public RivalCampaignWarService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, ProfileWisieService profileWisieService, TaskService taskService, CampaignService campaignService) {
        super(connectionService, taskGenerateService, taskRendererService, rivalGlobalService, rivalProfileSeasonService, profileWisieService, taskService);
        this.campaignService = campaignService;
    }

    @Override
    @Transactional
    public void disposeManager(PlayManager manager) {
        super.disposeManager(manager);
        PlayCampaignManager campaignManager = (PlayCampaignManager) manager;
        Long profileId = manager.getContainer().getInit().getCreatorProfile().getId();
        ProfileCampaign profileCampaign = campaignService.active(profileId).orElseThrow(IllegalArgumentException::new);
        campaignService.setProfileCampaignWisies(profileCampaign);
        if (profileCampaign.getProfile().equals(manager.getContainer().getResult().getWinner())) {
            profileCampaign.setPhase(profileCampaign.getPhase() + 1);
            profileCampaign.updateResourceGains();
            if (profileCampaign.getPhase() >= profileCampaign.getCampaign().getPhases()) {
                profileCampaign.setStatus(ProfileCampaignStatus.FINISHED);
                profileCampaign.setBookGain(campaignService.getBookGainForCampaign(profileCampaign.getCampaign()));
            }
            List<TeamMember> teamMembers = ((WarTeam) campaignManager.getContainer().getTeams().team(profileId)).getTeamMembers();
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

    public Optional<RivalCampaignWarInit> init() {
        Optional<ProfileCampaign> optionalProfileCampaign = campaignService.active();
        return optionalProfileCampaign.map(profileCampaign -> new RivalCampaignWarInit(CAMPAIGN_WAR, RivalImportance.FAST, profileCampaign.getProfile(), prepareComputerProfile(profileCampaign), profileCampaign));
    }

    public Profile prepareComputerProfile(ProfileCampaign profileCampaign) {
        boolean isLastPhase = profileCampaign.getPhase() == profileCampaign.getCampaign().getPhases() - 1;
        Profile computerProfile = prepareComputerProfile();
        Set<WisieType> wisieTypes = new HashSet<>();
        while (wisieTypes.size() < (Math.max(5 - profileCampaign.getPhase(), isLastPhase ? 5 : 1))) {
            wisieTypes.add(WisieType.random());
        }
        long profileWisieId = -1;
        List<ProfileCampaignWisie> profileCampaignWisies = profileCampaign.getWisies();
        double summaryValue = 0;
        for (ProfileCampaignWisie profileCampaignWisie : profileCampaignWisies) {
            if (!profileCampaignWisie.getDisabled()) {
                summaryValue += profileCampaignWisie.calculateValue();
            }
        }
        int rating = profileCampaign.getCampaign().getDifficultyLevel().getRating();

        for (WisieType wisieType : wisieTypes) {
            ProfileWisie profileWisie = new ProfileWisie(computerProfile, wisieType);
            profileWisie.setId(profileWisieId--);
            profileWisieService.initWisieAttributes(profileWisie);
            profileWisieService.initWisieHobbies(profileWisie, Category.random(((rating - 1) / 2) + 1));
            profileWisieService.initWisieSkills(profileWisie);
            int phaseDifficultyPromo = (isLastPhase ? 20 : 10) * profileCampaign.getPhase() * rating;
            double promo = Math.max(0, summaryValue / wisieTypes.size() + phaseDifficultyPromo - 5);
            for (MentalAttribute attribute : MentalAttribute.values()) {
                profileWisie.setMentalAttributeValue(attribute, profileWisie.getMentalAttributeValue(attribute) + promo);
            }
            for (WisdomAttribute attribute : WisdomAttribute.values()) {
                profileWisie.setWisdomAttributeValue(attribute, profileWisie.getWisdomAttributeValue(attribute) + promo);
            }
            computerProfile.getWisies().add(profileWisie);
        }
        return computerProfile;
    }

}
