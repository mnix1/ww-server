package com.ww.model.container.rival.war;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarModel extends RivalModel {

    private List<WisieAnswerManager> wisieAnswerManagers = new ArrayList<>();
    private Instant endChoosingWhoAnswerDate;
    private WarTeams teams;

    public WarModel(RivalInit init, WarTeams teams) {
        super(init, teams);
        this.teams = teams;
    }

    public void updateWisieAnswerManagers(WarManager manager) {
        Question question = findCurrentQuestion();
        wisieAnswerManagers = new CopyOnWriteArrayList<>();
        for (WarTeam warTeam : teams.getTeams()) {
            TeamMember teamMember = warTeam.getActiveTeamMember();
            if (teamMember.isWisie()) {
                wisieAnswerManagers.add(new WisieAnswerManager((WisieTeamMember) teamMember, warTeam, question, manager));
            }
        }
    }

    public List<WisieAnswerAction> getAnsweringWisieActions(WarTeam warTeam) {
        TeamMember teamMember = warTeam.getActiveTeamMember();
        if (!teamMember.isWisie()) {
            return null;
        }
        OwnedWisie wisie = ((WisieTeamMember) teamMember).getContent().getWisie();
        for (WisieAnswerManager answerManager : wisieAnswerManagers) {
            if (answerManager.getOwnedWisie().equals(wisie)) {
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
            if (manager.getProfileId().equals(profileId)) {
                return manager;
            }
        }
        return null;
    }

    public List<TeamMemberDTO> prepareTeam(List<TeamMember> teamMembers) {
        return teamMembers.stream().map(TeamMemberDTO::new).collect(Collectors.toList());
    }

    public String findChoosingTaskPropsTag() {
        List<WarTeam> profileContainers = new ArrayList<>(this.getTeams().getTeams());
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
        List<RivalTeam> profileContainers = new ArrayList<>(this.getTeams().getTeams());
        if (profileContainers.size() < 2) {
            return Optional.empty();
        }
        Integer presentIndexSize1 = ((WarTeam) profileContainers.get(0)).countPresentMembers();
        Integer presentIndexSize2 = ((WarTeam) profileContainers.get(1)).countPresentMembers();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return Optional.empty();
        }
        if (presentIndexSize1 == 0) {
            return Optional.of(profileContainers.get(1).getProfile());
        }
        return Optional.of(profileContainers.get(0).getProfile());
    }
}
