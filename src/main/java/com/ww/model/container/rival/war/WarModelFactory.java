package com.ww.model.container.rival.war;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalProfileContainer;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WarModelFactory extends RivalModelFactory {

    protected WarContainer container;

    public WarModelFactory(WarContainer container){
        this.container = container;
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModelBasic(model, profileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("team", container.prepareTeam(warProfileContainer.getTeamMembers()));
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getPresentIndexes());
            model.put("opponentActiveIndex", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getActiveIndex());
            model.put("opponentTeam", container.prepareTeam(container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getTeamMembers()));
        }
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModelPreparingNextTask(model, profileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("wisieActions", null);
        if (container.isOpponent()) {
            model.put("opponentActiveIndex", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getActiveIndex());
            model.put("opponentWisieActions", null);
        }
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModelAnswered(model, profileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModelAnswering(model, profileContainer);
        fillModelWisieAnswering(model, profileContainer);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalProfileContainer profileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        if (warProfileContainer.getActiveTeamMember().isWisie()) {
            List<WisieAnswerAction> wisieActions = container.getAnsweringWisieActions(warProfileContainer);
            if (wisieActions != null) {
                model.put("wisieActions", wisieActions);
            }
            if (container.isOpponent()) {
                List<WisieAnswerAction> opponentWisieActions = container.getAnsweringWisieActions(container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()));
                if (opponentWisieActions != null) {
                    model.put("opponentWisieActions", opponentWisieActions);
                }
            }
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModelAnsweringTimeout(model, profileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalProfileContainer profileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
        model.put("status", container.getStatus());
        model.put("choosingWhoAnswerInterval", Math.max(container.getEndChoosingWhoAnswerDate().toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("isChosenActiveIndex", warProfileContainer.isChosenActiveIndex());
        model.put("task", container.getTaskDTOs().get(container.getCurrentTaskIndex()).toTaskMeta());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer profileContainer) {
        super.fillModel(model, profileContainer);
        if (container.getStatus() == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, profileContainer);
        }
    }
}
