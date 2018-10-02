package com.ww.model.container.rival.war;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.rival.RivalPlayer;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.RivalTeamsContainer;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarContainer extends RivalContainer {

    private List<WisieAnswerManager> wisieAnswerManagers = new ArrayList<>();
    private Instant endChoosingWhoAnswerDate;
    private WarTeamsContainer teamsContainer;

    public WarContainer(RivalInitContainer container, WarTeamsContainer teamsContainer) {
        super(container, teamsContainer);
        this.teamsContainer = teamsContainer;
    }

    public void updateWisieAnswerManagers(WarManager manager) {
        wisieAnswerManagers = new ArrayList<>();
        for (WarProfileContainer warProfileContainer : teamsContainer.getProfileContainers()) {
            TeamMember teamMember = warProfileContainer.getActiveTeamMember();
            if (teamMember.isWisie()) {
                wisieAnswerManagers.add(new WisieAnswerManager((OwnedWisie) teamMember.getContent(), manager));
            }
        }
    }

    public List<WisieAnswerAction> getAnsweringWisieActions(WarProfileContainer warProfileContainer) {
        TeamMember teamMember = warProfileContainer.getActiveTeamMember();
        if (!teamMember.isWisie()) {
            return null;
        }
        OwnedWisie wisie = (OwnedWisie) teamMember.getContent();
        for (WisieAnswerManager answerManager : wisieAnswerManagers) {
            if (answerManager.getWisie().equals(wisie)) {
                List<WisieAnswerAction> actions = answerManager.getActions();
                return actions.subList(Math.max(0, actions.size() - 2), actions.size());
            }
        }
        return null;
    }

    public void startWisieAnswerManager() {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            manager.getFlow().start();
        }
    }

    public void stopWisieAnswerManager() {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            manager.getFlow().stop();
        }
    }

    public WisieAnswerManager getWisieAnswerManager(Long profileId) {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            if (manager.getWisie().getProfile().getId().equals(profileId)) {
                return manager;
            }
        }
        return null;
    }

    public List<TeamMemberDTO> prepareTeam(List<TeamMember> teamMembers) {
        return teamMembers.stream().map(TeamMemberDTO::new).collect(Collectors.toList());
    }

    public String findChoosingTaskPropsTag() {
        List<WarProfileContainer> profileContainers = new ArrayList<>(getTeamsContainer().getProfileContainers());
        if (profileContainers.size() < 2) {
            return null;
        }
        Integer presentIndexSize1 = profileContainers.get(0).countPresentMembers();
        Integer presentIndexSize2 = profileContainers.get(1).countPresentMembers();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return null;
        }
        if (presentIndexSize1.compareTo(presentIndexSize2) < 0) {
            return profileContainers.get(0).getProfile().getTag();
        }
        return profileContainers.get(1).getProfile().getTag();
    }

    public Optional<Profile> findWinner() {
        List<RivalProfileContainer> profileContainers = new ArrayList<>(getTeamsContainer().getProfileContainers());
        if (profileContainers.size() < 2) {
            return Optional.empty();
        }
        Integer presentIndexSize1 = ((WarProfileContainer) profileContainers.get(0)).countPresentMembers();
        Integer presentIndexSize2 = ((WarProfileContainer) profileContainers.get(1)).countPresentMembers();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return Optional.empty();
        }
        if (presentIndexSize1 == 0) {
            return Optional.of(profileContainers.get(1).getProfile());
        }
        return Optional.of(profileContainers.get(0).getProfile());
    }
}
