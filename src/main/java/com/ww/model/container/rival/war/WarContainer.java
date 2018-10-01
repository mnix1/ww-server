package com.ww.model.container.rival.war;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
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
    List<WisieAnswerManager> wisieAnswerManagers = new ArrayList<>();
    protected Instant endChoosingWhoAnswerDate;

    public WarProfileContainer opponentProfileContainer(Long id) {
        return (WarProfileContainer) profileContainers.get(opponents.get(id));
    }

    public void updateWisieAnswerManagers(RivalManager rivalManager) {
        wisieAnswerManagers = new ArrayList<>();
        for (RivalProfileContainer rivalProfileContainer : profileContainers.values()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            TeamMember teamMember = warProfileContainer.getActiveTeamMember();
            if (teamMember.isWisie()) {
                wisieAnswerManagers.add(new WisieAnswerManager((OwnedWisie) teamMember.getContent(), rivalManager));
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
            manager.start();
        }
    }

    public void stopWisieAnswerManager() {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            manager.stop();
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
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileContainers().values());
        if (rivalProfileContainers.size() < 2) {
            return null;
        }
        Integer presentIndexSize1 = ((WarProfileContainer) rivalProfileContainers.get(0)).countPresentMembers();
        Integer presentIndexSize2 = ((WarProfileContainer) rivalProfileContainers.get(1)).countPresentMembers();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return null;
        }
        if (presentIndexSize1.compareTo(presentIndexSize2) < 0) {
            return rivalProfileContainers.get(0).getProfile().getTag();
        }
        return rivalProfileContainers.get(1).getProfile().getTag();
    }

    public Optional<Profile> findWinner() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileContainers().values());
        if (rivalProfileContainers.size() < 2) {
            return Optional.empty();
        }
        Integer presentIndexSize1 = ((WarProfileContainer) rivalProfileContainers.get(0)).countPresentMembers();
        Integer presentIndexSize2 = ((WarProfileContainer) rivalProfileContainers.get(1)).countPresentMembers();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return Optional.empty();
        }
        if (presentIndexSize1 == 0) {
            return Optional.of(rivalProfileContainers.get(1).getProfile());
        }
        return Optional.of(rivalProfileContainers.get(0).getProfile());
    }

    public WarProfileContainer profileContainer(Long id) {
        return (WarProfileContainer) super.profileContainer(id);
    }
}
