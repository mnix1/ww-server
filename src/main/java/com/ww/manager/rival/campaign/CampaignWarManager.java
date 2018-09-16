package com.ww.manager.rival.campaign;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.campaign.state.CampaignWarStateChoosingTaskProps;
import com.ww.manager.rival.campaign.state.CampaignWarStateChosenWhoAnswer;
import com.ww.manager.rival.state.StateClose;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.social.ProfileConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CampaignWarManager extends WarManager {
    public static final Long BOT_PROFILE_ID = -1L;

    public ProfileCampaign profileCampaign;

    public static boolean isBotProfile(Profile profile) {
        return profile.getId().equals(BOT_PROFILE_ID);
    }

    public CampaignWarManager(RivalInitContainer container, RivalCampaignWarService rivalCampaignWarService, ProfileConnectionService profileConnectionService, ProfileCampaign profileCampaign) {
        this.abstractRivalService = rivalCampaignWarService;
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
        this.profileCampaign = profileCampaign;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, ProfileCampaign profileCampaign) {
        if (isBotProfile(profile)) {
            return TeamHelper.prepareTeamMembers(new ArrayList<>(profile.getWisies()));
        }
        return prepareTeamMembers(profileCampaign, profileCampaign.getWisies());
    }


    protected List<TeamMember> prepareTeamMembers(ProfileCampaign profileCampaign, List<ProfileCampaignWisie> wisies) {
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profileCampaign.getProfile(), wisies, rivalContainer.getImportance(), rivalContainer.getType());
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.isWisie()) {
                for (ProfileCampaignWisie wisie : wisies) {
                    if (wisie.equals(teamMember.getContent())) {
                        teamMember.setPresent(!wisie.getDisabled());
                    }
                }
            } else {
                teamMember.setPresent(profileCampaign.getPresent());
            }
        }
        return teamMembers;
    }

    public synchronized void phase2() {
        if (isEnd()) {
            new StateClose(this).startVoid();
        } else {
            new CampaignWarStateChoosingTaskProps(this).startVoid();
            phase3();
        }
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new CampaignWarStateChosenWhoAnswer(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
