package com.ww.manager.rival.war;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.state.*;
import com.ww.model.container.rival.battle.BattleFlow;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.model.container.rival.war.*;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;
import static com.ww.service.rival.global.RivalMessageService.HINT;

@NoArgsConstructor
@Getter
public class WarManager extends RivalManager {

    public WarContainer container;
    public WarModelFactory modelFactory;
    public WarInterval interval;
    public WarFlow flow;

    public WarManager(RivalTwoPlayerInitContainer container, RivalWarService rivalWarService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalWarService;
        this.profileConnectionService = profileConnectionService;
        Profile creator = container.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalWarService.getProfileWisies(creator);
        Profile opponent = container.getOpponentProfile();
        List<ProfileWisie> opponentWisies = rivalWarService.getProfileWisies(opponent);
        this.container = new WarContainer(container, new WarTeamsContainer());
        this.container.getTeamsContainer().addProfile(creator.getId(), new WarProfileContainer(creator, prepareTeamMembers(creator, creatorWisies)));
        this.container.getTeamsContainer().addProfile(opponent.getId(), new WarProfileContainer(opponent, prepareTeamMembers(opponent, opponentWisies)));
        this.modelFactory = new WarModelFactory(this.container);
        this.interval = new WarInterval();
        this.flow = new WarFlow(this);
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        if (super.processMessage(profileId, content)) {
            return true;
        }
        String id = (String) content.get("id");
        if (id.equals(CHOOSE_WHO_ANSWER)) {
            flow.chosenWhoAnswer(profileId, content);
        } else if (id.equals(HINT)) {
            flow.hint(profileId, content);
        } else {
            return false;
        }
        return true;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        return TeamHelper.prepareTeamMembers(profile, wisies, container.getImportance(), container.getType());
    }

    public boolean isEnd() {
        for (WarProfileContainer warProfileContainer : getContainer().getTeamsContainer().getProfileContainers()) {
            if (!warProfileContainer.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }
}
