package com.ww.manager.rival.campaign;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.campaign.CampaignWarFlow;
import com.ww.model.container.rival.campaign.CampaignWarModel;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.container.rival.war.skill.EmptyTeamSkills;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.websocket.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.TeamHelper.isBotProfile;

public class CampaignWarManager extends WarManager {
    public ProfileCampaign profileCampaign;

    public CampaignWarManager(RivalCampaignWarInit init, RivalCampaignWarService rivalService) {
        this.rivalService = rivalService;
        this.profileCampaign = init.getProfileCampaign();
        WarTeams teams = new WarTeams();
        this.model = new CampaignWarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new WarInterval();
        this.flow = new CampaignWarFlow(this);

        Profile creator = init.getCreatorProfile();
        List<TeamMember> teamMembers = prepareTeamMembers(creator, profileCampaign);
        WarTeam creatorTeam = new WarTeam(creator, teamMembers, new WarTeamSkills(1, teamMembers));
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
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        if (profileCampaign.getPresent()) {
            teamMembers.add(new WisorTeamMember(index++, profileCampaign.getProfile(), new ExtendedProfileDTO(profileCampaign.getProfile())));
        }
        for (ProfileCampaignWisie wisie : wisies) {
            if (wisie.getDisabled()) {
                continue;
            }
            WarWisie warWisie = new WarWisie(wisie);
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    @Override
    public Message getMessageContent() {
        return Message.CAMPAIGN_WAR_CONTENT;
    }

}
