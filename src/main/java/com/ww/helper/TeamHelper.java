package com.ww.helper;

import com.ww.model.constant.Category;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.war.*;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamHelper {
    public static final Long BOT_PROFILE_ID = -1L;

    public static boolean isBotProfile(Profile profile) {
        return profile.getId().equals(BOT_PROFILE_ID);
    }

    public static List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        teamMembers.add(new WisorTeamMember(index++, profile, new ExtendedProfileDTO(profile)));
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie);
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    public static List<TeamMember> prepareTeamMembers(List<? extends OwnedWisie> wisies) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie);
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
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
                int index = teamMembers.stream().filter(teamMember -> teamMember.isWisie() && wisie.equals(((WarWisie) teamMember.getContent()).getWisie())).findFirst().get().getIndex();
                presentIndexes.add(index);
            }
        }
        return presentIndexes;
    }

    public static List<TeamMemberDTO> mapToTeamDTOs(List<TeamMember> teamMembers) {
        return teamMembers.stream().map(TeamMemberDTO::new).collect(Collectors.toList());
    }

    public static List<WarTeam> mapToWarTeams(Collection<RivalTeam> teams){
        return teams.stream().map(team -> ((WarTeam) team)).collect(Collectors.toList());
    }
    public static List<BattleTeam> mapToBattleTeams(Collection<RivalTeam> teams){
        return teams.stream().map(team -> ((BattleTeam) team)).collect(Collectors.toList());
    }

    public static WarTeam teamWithLowestCountPresentMembers(List<WarTeam> warTeams){
        int minIndex = IntStream.range(0, warTeams.size())
                .reduce((i, j) -> warTeams.get(i).countPresentMembers() > warTeams.get(j).countPresentMembers() ? j : i)
                .getAsInt();
        return warTeams.get(minIndex);
    }

    public static BattleTeam teamWithLowestScore(List<BattleTeam> battleTeams){
        int minIndex = IntStream.range(0, battleTeams.size())
                .reduce((i, j) -> battleTeams.get(i).getScore() > battleTeams.get(j).getScore() ? j : i)
                .getAsInt();
        return battleTeams.get(minIndex);
    }

    public static boolean teamsHaveSameCountPresentMembers(Collection<RivalTeam> teams){
        Set<Integer> presentMemberCounts = teams.stream().map(team -> ((WarTeam) team).countPresentMembers()).collect(Collectors.toSet());
        return presentMemberCounts.size() == 1;
    }
    public static boolean teamsHaveSameScore(Collection<RivalTeam> teams){
        Set<Integer> presentMemberCounts = teams.stream().map(team -> ((BattleTeam) team).getScore()).collect(Collectors.toSet());
        return presentMemberCounts.size() == 1;
    }

    public static int findIndexOfWisieWithHobby(WarTeam warTeam, Category category){
        for (TeamMember teamMember : warTeam.getTeamMembers()) {
            if (!teamMember.isWisie() || !teamMember.isPresent()) {
                continue;
            }
            WisieTeamMember wisieTeamMember = (WisieTeamMember) teamMember;
            if (wisieTeamMember.getContent().getWisie().getHobbies().contains(category)) {
                return wisieTeamMember.getIndex();
            }
        }
        return warTeam.getPresentIndexes().get(0);
    }
}
