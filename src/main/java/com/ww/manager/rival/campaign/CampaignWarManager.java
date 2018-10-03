package com.ww.manager.rival.campaign;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.EmptyTeamSkills;
import com.ww.model.container.rival.campaign.CampaignWarModel;
import com.ww.model.container.rival.campaign.CampaignWarFlow;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.ArrayList;
import java.util.List;

public class CampaignWarManager extends WarManager {
    public static final Long BOT_PROFILE_ID = -1L;

    public ProfileCampaign profileCampaign;

    public static boolean isBotProfile(Profile profile) {
        return profile.getId().equals(BOT_PROFILE_ID);
    }

    public CampaignWarManager(RivalCampaignWarInit init, RivalCampaignWarService rivalService) {
        this.rivalService = rivalService;
        this.profileCampaign = init.getProfileCampaign();
        WarTeams teams = new WarTeams();
        this.model = new CampaignWarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new WarInterval();
        this.flow = new CampaignWarFlow(this);

        Profile creator = init.getCreatorProfile();
        WarTeam creatorTeam = new WarTeam(creator, prepareTeamMembers(creator, profileCampaign), new WarTeamSkills(1, profileCampaign.getWisies()));
        teams.addProfile(creator.getId(), creatorTeam);

        Profile opponent = init.getOpponentProfile();
        WarTeam opponentTeam = new WarTeam(opponent, prepareTeamMembers(opponent, profileCampaign), new EmptyTeamSkills());
        teams.addProfile(opponent.getId(), opponentTeam);
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, ProfileCampaign profileCampaign) {
        if (isBotProfile(profile)) {
            return TeamHelper.prepareTeamMembers(new ArrayList<>(profile.getWisies()));
        }
        return prepareTeamMembers(profileCampaign, profileCampaign.getWisies());
    }

    protected List<TeamMember> prepareTeamMembers(ProfileCampaign profileCampaign, List<ProfileCampaignWisie> wisies) {
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profileCampaign.getProfile(), wisies, model.getImportance(), model.getType());
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

    @Override
    public Message getMessageContent() {
        return Message.CAMPAIGN_WAR_CONTENT;
    }

}
