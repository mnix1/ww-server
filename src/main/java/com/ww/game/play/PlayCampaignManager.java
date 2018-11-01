package com.ww.game.play;

import com.ww.helper.TeamHelper;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.container.rival.war.skill.EmptyTeamSkills;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.game.play.communication.PlayWarCommunication;
import com.ww.game.play.container.PlayCampaignContainer;
import com.ww.game.play.container.PlayWarContainer;
import com.ww.game.play.flow.PlayCampaignFlow;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.websocket.message.Message;

import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.TeamHelper.isBotProfile;

public class PlayCampaignManager extends PlayManager {
    public PlayCampaignManager(RivalCampaignWarInit init, RivalCampaignWarService rivalService) {
        super(rivalService);
        this.container = new PlayCampaignContainer(init, prepareTeams(init), prepareTasks(), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayCampaignFlow(this, new WarInterval());
        this.communication = new PlayWarCommunication(this, Message.CAMPAIGN_WAR_CONTENT);
    }

    protected RivalTeams prepareTeams(RivalCampaignWarInit init) {
        Profile creatorProfile = init.getCreatorProfile();
        List<TeamMember> teamMembers = prepareTeamMembers(creatorProfile, init.getProfileCampaign());
        WarTeam creatorTeam = new WarTeam(creatorProfile, teamMembers, new WarTeamSkills(1, teamMembers));

        Profile opponent = init.getOpponentProfile();
        WarTeam opponentTeam = new WarTeam(opponent, prepareTeamMembers(opponent, init.getProfileCampaign()), new EmptyTeamSkills());
        return new RivalTeams(creatorTeam, opponentTeam);
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
            teamMembers.add(new WisieTeamMember(index++, warWisie));
        }
        return teamMembers;
    }

}
