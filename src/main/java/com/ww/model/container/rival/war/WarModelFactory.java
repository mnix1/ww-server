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

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("team", container.prepareTeam(warProfileContainer.getTeamMembers()));
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
            model.put("opponentActiveIndex", container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
            model.put("opponentTeam", container.prepareTeam(container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getTeamMembers()));
        }
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelPreparingNextTask(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("wisieActions", null);
        if (container.isOpponent()) {
            model.put("opponentActiveIndex", container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
            model.put("opponentWisieActions", null);
        }
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
        }
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswering(model, rivalProfileContainer);
        fillModelWisieAnswering(model, rivalProfileContainer);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        if (warProfileContainer.getActiveTeamMember().isWisie()) {
            List<WisieAnswerAction> wisieActions = container.getAnsweringWisieActions(warProfileContainer);
            if (wisieActions != null) {
                model.put("wisieActions", wisieActions);
            }
            if (container.isOpponent()) {
                List<WisieAnswerAction> opponentWisieActions = container.getAnsweringWisieActions(container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()));
                if (opponentWisieActions != null) {
                    model.put("opponentWisieActions", opponentWisieActions);
                }
            }
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnsweringTimeout(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
        }
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("status", container.getStatus());
        model.put("choosingWhoAnswerInterval", Math.max(container.endChoosingWhoAnswerDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("isChosenActiveIndex", warProfileContainer.isChosenActiveIndex());
        model.put("task", container.getTaskDTOs().get(container.getCurrentTaskIndex()).toTaskMeta());
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModel(model, rivalProfileContainer);
        if (container.getStatus() == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, rivalProfileContainer);
        }
    }
}
