package com.ww.model.container.rival.war;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeamContainer;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WarModelFactory extends RivalModelFactory {

    protected WarContainer container;

    public WarModelFactory(WarContainer container) {
        this.container = container;
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelBasic(model, profileContainer);
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
//        model.put("skills", container.prepareSkills())
        model.put("team", container.prepareTeam(warProfileContainer.getTeamMembers()));
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getPresentIndexes());
            model.put("opponentActiveIndex", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getActiveIndex());
            model.put("opponentTeam", container.prepareTeam(container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getTeamMembers()));
        }
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelPreparingNextTask(model, profileContainer);
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("wisieActions", null);
        if (container.isOpponent()) {
            model.put("opponentActiveIndex", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getActiveIndex());
            model.put("opponentWisieActions", null);
        }
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelAnswered(model, profileContainer);
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModelAnswering(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelAnswering(model, profileContainer);
        fillModelWisieAnswering(model, profileContainer);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalTeamContainer profileContainer) {
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        if (warProfileContainer.getActiveTeamMember().isWisie()) {
//            fillModelAnsweringSkills(model, warProfileContainer);
            List<WisieAnswerAction> wisieActions = container.getAnsweringWisieActions(warProfileContainer);
            if (wisieActions != null) {
                model.put("wisieActions", wisieActions);
            }
            if (container.isOpponent()) {
                List<WisieAnswerAction> opponentWisieActions = container.getAnsweringWisieActions(container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()));
                if (opponentWisieActions != null) {
                    model.put("opponentWisieActions", opponentWisieActions);
                }
            }
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModelAnsweringTimeout(model, profileContainer);
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalTeamContainer profileContainer) {
        WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
        model.put("status", container.getStatus());
        model.put("choosingWhoAnswerInterval", Math.max(container.getEndChoosingWhoAnswerDate().toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("isChosenActiveIndex", warProfileContainer.isChosenActiveIndex());
        model.put("task", container.getTaskDTOs().get(container.getCurrentTaskIndex()).toTaskMeta());
//        fillModelChoosingWhoAnswerSkills(model, warProfileContainer);
        if (container.isOpponent()) {
            model.put("opponentPresentIndexes", container.getTeamsContainer().opponentTeamContainer(profileContainer.getProfileId()).getPresentIndexes());
        }
    }

    public void fillModel(Map<String, Object> model, RivalTeamContainer profileContainer) {
        super.fillModel(model, profileContainer);
        if (container.getStatus() == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, profileContainer);
        }
    }
//
//    public Map<String,Object> prepareSkills(Map<String, Object> model, WarTeamContainer teamContainer) {
//        fillModelAnsweringSkills(model, teamContainer);
//        fillModelChoosingWhoAnswerSkills(model, teamContainer);
//    }
//
//    public void fillModelAnsweringSkills(Map<String, Object> model, WarTeamContainer teamContainer) {
//        fillIfMoreThen0(model, "hints", teamContainer.getHints());
//        fillIfMoreThen0(model, "waterPistols", teamContainer.getWaterPistols());
//    }
//
//    public void fillModelChoosingWhoAnswerSkills(Map<String, Object> model, WarTeamContainer teamContainer) {
//        fillIfMoreThen0(model, "lifebuoys", teamContainer.getLifebuoys());
//    }
//
//    private void fillIfMoreThen0(Map<String, Object> model, String key, int value) {
//        if (value > 0) {
//            model.put(key, value);
//        }
//    }
}
