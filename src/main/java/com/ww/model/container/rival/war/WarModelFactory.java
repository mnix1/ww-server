package com.ww.model.container.rival.war;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WarModelFactory extends RivalModelFactory {

    protected WarModel model;

    public WarModelFactory(WarModel model) {
        this.model = model;
    }

    protected WarTeam opponentTeam(RivalTeam team) {
        return this.model.getTeams().opponentTeam(team.getProfileId());
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeam team) {
        super.fillModelBasic(model, team);
        WarTeam warTeam = (WarTeam) team;
        model.put("activeIndex", warTeam.getActiveIndex());
        model.put("team", this.model.prepareTeam(warTeam.getTeamMembers()));
        if (this.model.isOpponent()) {
            WarTeam opponentTeam = opponentTeam(team);
            model.put("opponentActiveIndex", opponentTeam.getActiveIndex());
            model.put("opponentTeam", this.model.prepareTeam(opponentTeam.getTeamMembers()));
        }
        fillModelPresentIndexes(model, team);
        fillModelSkills(model, team);
    }

    public void fillModelSkills(Map<String, Object> model, RivalTeam team) {
        WarTeam warTeam = (WarTeam) team;
        model.put("skills", warTeam.getTeamSkills().prepareSkills());
        if (this.model.isOpponent()) {
            WarTeam opponentTeam = opponentTeam(team);
            model.put("opponentSkills", opponentTeam.getTeamSkills().prepareSkills());
        }
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalTeam team) {
        super.fillModelPreparingNextTask(model, team);
        WarTeam warTeam = (WarTeam) team;
        model.put("activeIndex", warTeam.getActiveIndex());
        model.put("wisieActions", null);
        if (this.model.isOpponent()) {
            model.put("opponentActiveIndex", opponentTeam(team).getActiveIndex());
            model.put("opponentWisieActions", null);
        }
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeam team) {
        super.fillModelAnswered(model, team);
        fillModelPresentIndexes(model, team);
    }

    public void fillModelAnswering(Map<String, Object> model, RivalTeam team) {
        super.fillModelAnswering(model, team);
        fillModelWisieAnswering(model, team);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalTeam team) {
        WarTeam warTeam = (WarTeam) team;
        if (warTeam.getActiveTeamMember().isWisie()) {
            List<WisieAnswerAction> wisieActions = this.model.getAnsweringWisieActions(warTeam);
            if (wisieActions != null) {
                model.put("wisieActions", wisieActions);
            }
            if (this.model.isOpponent()) {
                List<WisieAnswerAction> opponentWisieActions = this.model.getAnsweringWisieActions(opponentTeam(team));
                if (opponentWisieActions != null) {
                    model.put("opponentWisieActions", opponentWisieActions);
                }
            }
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalTeam team) {
        super.fillModelAnsweringTimeout(model, team);
        fillModelPresentIndexes(model, team);
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalTeam team) {
        WarTeam warTeam = (WarTeam) team;
        model.put("status", this.model.getStatus());
        model.put("choosingWhoAnswerInterval", Math.max(this.model.getEndChoosingWhoAnswerDate().toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warTeam.getActiveIndex());
        model.put("isChosenActiveIndex", warTeam.isChosenActiveIndex());
        model.put("task", this.model.getTaskDTOs().get(this.model.getCurrentTaskIndex()).toTaskMeta());
        fillModelPresentIndexes(model, team);
    }

    public void fillModelPresentIndexes(Map<String, Object> model, RivalTeam team) {
        WarTeam warTeam = (WarTeam) team;
        model.put("presentIndexes", warTeam.getPresentIndexes());
        if (this.model.isOpponent()) {
            model.put("opponentPresentIndexes", opponentTeam(team).getPresentIndexes());
        }
    }

    public void fillModel(Map<String, Object> model, RivalTeam team) {
        super.fillModel(model, team);
        if (this.model.getStatus() == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, team);
        }
    }
}
