package com.ww.model.container.rival.war;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarContainer extends RivalContainer {
    List<WisieAnswerManager> wisieAnswerManagers = new ArrayList<>();
    protected Instant endChoosingWhoAnswerDate;

    public void updateWisieAnswerManagers(RivalManager rivalManager) {
        wisieAnswerManagers = new ArrayList<>();
        forEachProfile(rivalProfileContainer -> {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            TeamMember teamMember = warProfileContainer.getActiveTeamMember();
            if (teamMember.isWisie()) {
                wisieAnswerManagers.add(new WisieAnswerManager((ProfileWisie) teamMember.getContent(), rivalManager));
            }
        });
    }

    private List<WisieAnswerAction> getAnsweringWisieActions(WarProfileContainer warProfileContainer) {
        TeamMember teamMember = warProfileContainer.getActiveTeamMember();
        if (!teamMember.isWisie()) {
            return null;
        }
        ProfileWisie wisie = (ProfileWisie) teamMember.getContent();
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

    private List<TeamMemberDTO> prepareTeam(List<TeamMember> teamMembers) {
        return teamMembers.stream().map(TeamMemberDTO::new).collect(Collectors.toList());
    }

    public String findChoosingTaskPropsTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
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
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
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

    public WarProfileContainer getRivalProfileContainer(Long id) {
        return (WarProfileContainer) super.getRivalProfileContainer(id);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("team", prepareTeam(warProfileContainer.getTeamMembers()));
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        model.put("opponentTeam", prepareTeam(getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getTeamMembers()));
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelPreparingNextTask(model, rivalProfileContainer);
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("wisieActions", null);
        model.put("opponentWisieActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswering(model, rivalProfileContainer);
        fillModelWisieAnswering(model, rivalProfileContainer);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        if (warProfileContainer.getActiveTeamMember().isWisie()) {
            List<WisieAnswerAction> wisieActions = getAnsweringWisieActions(warProfileContainer);
            if (wisieActions != null) {
                model.put("wisieActions", wisieActions);
            }
            List<WisieAnswerAction> opponentWisieActions = getAnsweringWisieActions(getRivalProfileContainer(rivalProfileContainer.getOpponentId()));
            if (opponentWisieActions != null) {
                model.put("opponentWisieActions", opponentWisieActions);
            }
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnsweringTimeout(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("wisieActions", null);
        model.put("opponentWisieActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("status", status);
        model.put("choosingWhoAnswerInterval", Math.max(endChoosingWhoAnswerDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("isChosenActiveIndex", warProfileContainer.isChosenActiveIndex());
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModel(model, rivalProfileContainer);
        if (status == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, rivalProfileContainer);
        }
    }
}
