package com.ww.manager.rival.war;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
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

    public WarContainer container;
    public WarModelFactory modelFactory;
    public WarInterval interval;
    public WarFlow flow;

    public WarManager(RivalTwoPlayerInitContainer initContainer, RivalWarService rivalWarService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalWarService;
        this.profileConnectionService = profileConnectionService;
        WarTeamsContainer teams = new WarTeamsContainer();

        Profile creator = initContainer.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalWarService.getProfileWisies(creator);
        WarTeamContainer creatorTeam = new WarTeamContainer(creator, prepareTeamMembers(creator, creatorWisies), new WarTeamSkillsContainer(1, creatorWisies));
        teams.addProfile(creator.getId(), creatorTeam);

        Profile opponent = initContainer.getOpponentProfile();
        List<ProfileWisie> opponentWisies = rivalWarService.getProfileWisies(opponent);
        WarTeamContainer opponentTeam = new WarTeamContainer(opponent, prepareTeamMembers(opponent, opponentWisies), new WarTeamSkillsContainer(1, opponentWisies));
        teams.addProfile(opponent.getId(), opponentTeam);

        this.container = new WarContainer(initContainer, teams);
        this.modelFactory = new WarModelFactory(this.container);
        this.interval = new WarInterval();
        this.flow = new WarFlow(this);
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        return TeamHelper.prepareTeamMembers(profile, wisies, container.getImportance(), container.getType());
    }

    public boolean isEnd() {
        for (WarTeamContainer warProfileContainer : getContainer().getTeamsContainer().getTeamContainers()) {
            if (!warProfileContainer.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }
}
