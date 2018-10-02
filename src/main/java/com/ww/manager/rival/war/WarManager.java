package com.ww.manager.rival.war;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarManager extends RivalManager {

    public WarModel model;
    public WarModelFactory modelFactory;
    public WarInterval interval;
    public WarFlow flow;

    public WarManager(RivalTwoPlayerInit init, RivalWarService rivalService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalService;
        this.profileConnectionService = profileConnectionService;
        WarTeams teams = new WarTeams();
        this.model = new WarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new WarInterval();
        this.flow = new WarFlow(this);

        Profile creator = init.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalService.getProfileWisies(creator);
        WarTeam creatorTeam = new WarTeam(creator, prepareTeamMembers(creator, creatorWisies), new WarTeamSkillsContainer(1, creatorWisies));
        teams.addProfile(creator.getId(), creatorTeam);

        Profile opponent = init.getOpponentProfile();
        List<ProfileWisie> opponentWisies = rivalService.getProfileWisies(opponent);
        WarTeam opponentTeam = new WarTeam(opponent, prepareTeamMembers(opponent, opponentWisies), new WarTeamSkillsContainer(1, opponentWisies));
        teams.addProfile(opponent.getId(), opponentTeam);
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        return TeamHelper.prepareTeamMembers(profile, wisies, model.getImportance(), model.getType());
    }

    public boolean isEnd() {
        for (WarTeam warProfileContainer : this.getModel().getTeamsContainer().getTeamContainers()) {
            if (!warProfileContainer.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }
}
