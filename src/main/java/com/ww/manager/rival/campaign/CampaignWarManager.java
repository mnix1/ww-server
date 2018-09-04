package com.ww.manager.rival.campaign;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.campaign.CampaignWarContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.campaign.CampaignWarService;
import com.ww.service.social.ProfileConnectionService;

import java.util.ArrayList;
import java.util.List;

import static com.ww.service.rival.campaign.CampaignWarService.BOT_PROFILE_ID;

public class CampaignWarManager extends WarManager {

    public CampaignWarContainer campaignWarContainer;

    public CampaignWarManager(RivalInitContainer container, CampaignWarService campaignWarService, ProfileConnectionService profileConnectionService) {
        super(container, campaignWarService, profileConnectionService);
//        this.rivalService = campaignService;
//        this.profileConnectionService = profileConnectionService;
//        Long creatorId = container.getCreatorProfile().getId();
//        this.rivalContainer = new CampaignWarContainer();
//        this.rivalContainer.storeInformationFromInitContainer(container);
//        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(container.getCreatorProfile(), campaignService.getProfileWisies(creatorId), null));
//        this.warContainer = (WarContainer) this.rivalContainer;
//        this.campaignWarContainer = (CampaignWarContainer) this.rivalContainer;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<ProfileWisie> wisies) {
        if (!profile.getId().equals(BOT_PROFILE_ID)) {
            return super.prepareTeamMembers(profile, wisies);
        }
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (ProfileWisie wisie : wisies) {
            teamMembers.add(new TeamMember(index++, HeroType.WISIE, wisie, new WarProfileWisieDTO(wisie)));
        }
        return teamMembers;
    }

}
