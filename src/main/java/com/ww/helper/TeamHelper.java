package com.ww.helper;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.OwnedWisie;
import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.model.entity.wisie.ProfileWisie;

import java.util.ArrayList;
import java.util.List;

public class TeamHelper {
    public static List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies, RivalImportance importance, RivalType type) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        teamMembers.add(new TeamMember(index++, HeroType.WISOR, profile, importance == RivalImportance.RANKING ? new RivalProfileDTO(profile, type) : new ProfileDTO(profile)));
        for (OwnedWisie wisie : wisies) {
            teamMembers.add(new TeamMember(index++, HeroType.WISIE, wisie, new WarProfileWisieDTO(wisie)));
        }
        return teamMembers;
    }

    public static List<TeamMember> prepareTeamMembers(List<? extends OwnedWisie> wisies) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (OwnedWisie wisie : wisies) {
            teamMembers.add(new TeamMember(index++, HeroType.WISIE, wisie, new WarProfileWisieDTO(wisie)));
        }
        return teamMembers;
    }

    public static List<Integer> preparePresentIndexes(ProfileCampaign profileCampaign, List<TeamMember> teamMembers) {
        List<Integer> presentIndexes = new ArrayList<>();
        if (profileCampaign.getPresent()) {
            presentIndexes.add(0);
        }
        for (ProfileCampaignWisie wisie : profileCampaign.getWisies()) {
            if (!wisie.getDisabled()) {
                int index = teamMembers.stream().filter(teamMember -> teamMember.isWisie() && wisie.equals(teamMember.getContent())).findFirst().get().getIndex();
                presentIndexes.add(index);
            }
        }
        return presentIndexes;
    }
}
