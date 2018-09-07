package com.ww.manager.rival.campaign;

import com.ww.manager.rival.campaign.state.CampaignWarStateChosenWhoAnswer;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.campaign.CampaignWarContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import com.ww.model.entity.social.Profile;
import com.ww.service.rival.campaign.CampaignWarService;
import com.ww.service.social.ProfileConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CampaignWarManager extends WarManager {
    public static final Long BOT_PROFILE_ID = -1L;

    public CampaignWarContainer campaignWarContainer;

    public CampaignWarManager(RivalInitContainer container, CampaignWarService campaignWarService, ProfileConnectionService profileConnectionService, ProfileCampaign profileCampaign) {
        this.rivalService = campaignWarService;
        this.profileConnectionService = profileConnectionService;
        Profile creator = container.getCreatorProfile();
        Long creatorId = creator.getId();
        Profile opponent = container.getOpponentProfile();
        Long opponentId = opponent.getId();
        this.rivalContainer = new WarContainer();
        this.rivalContainer.storeInformationFromInitContainer(container);
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(creator, opponentId, prepareTeamMembers(creator, profileCampaign)));
        this.rivalContainer.addProfile(opponentId, new WarProfileContainer(opponent, creatorId, prepareTeamMembers(opponent, profileCampaign)));
        this.warContainer = (WarContainer) this.rivalContainer;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, ProfileCampaign profileCampaign) {
        if (profile.getId().equals(BOT_PROFILE_ID)) {
            return super.prepareTeamMembers(profile, new ArrayList<>(profile.getWisies()));
        }
        return super.prepareTeamMembers(profileCampaign.getProfile(), profileCampaign.getWisies());
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new CampaignWarStateChosenWhoAnswer(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
