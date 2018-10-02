package com.ww.model.container.rival;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public abstract class RivalModelFactory {
    public abstract RivalModel getModel();

    public RivalProfileDTO prepareProfile(Profile profile) {
        return new RivalProfileDTO(profile, getModel().type);
    }

    public void fillModelBasic(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("importance", getModel().importance.name());
        model.put("type", getModel().type.name());
        model.put("profile", prepareProfile(team.getProfile()));
        if (getModel().isOpponent()) {
            model.put("opponent", prepareProfile(getModel().getTeams().opponentTeam(team.getProfileId()).getProfile()));
        }
    }

    public void fillModelIntro(Map<String, Object> model, RivalTeam team) {
        fillModelBasic(model, team);
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(getModel().nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex));
        model.put("correctAnswerId", null);
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
        model.put("endAnsweringInterval", Math.max(getModel().endAnsweringDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswered(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("correctAnswerId", getModel().findCurrentCorrectAnswerId());
        model.put("markedAnswerId", getModel().markedAnswerId);
        model.put("meAnswered", getModel().answeredProfileId.equals(team.getProfileId()));
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("correctAnswerId", getModel().findCurrentCorrectAnswerId());
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
    }

    public void fillModelChoosingTaskPropsTimeout(Map<String, Object> model) {
        model.put("status", getModel().status);
        model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex).toTaskMeta());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("choosingTaskPropsInterval", Math.max(getModel().endChoosingTaskPropsDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("choosingTaskPropsTag", getModel().findChoosingTaskPropsTag());
        model.put("taskId", getModel().currentTaskIndex + 1);
        if (getModel().randomChooseTaskProps()) {
            model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex).toTaskMeta());
        } else {
            model.put("task", null);
            model.put("chosenCategory", getModel().chosenCategory);
            model.put("chosenDifficulty", getModel().chosenDifficulty);
            model.put("isChosenCategory", getModel().isChosenCategory);
            model.put("isChosenDifficulty", getModel().isChosenDifficulty);
        }
    }

    public void fillModelChosenTaskProps(Map<String, Object> model) {
        model.put("status", getModel().status);
        model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex).toTaskMeta());
    }

    public void fillModelClosed(Map<String, Object> model, RivalTeam team) {
        model.put("status", getModel().status);
        model.put("isDraw", getModel().draw);
        if (!getModel().draw) {
            model.put("winnerTag", getModel().winner.getTag());
        }
        model.put("resigned", getModel().resigned);
    }

    public void fillModelEloChanged(Map<String, Object> model, RivalTeam team) {
        if (!getModel().isRanking()) {
            return;
        }
        model.put("newProfile", prepareProfile(team.getProfile()));
        if (getModel().isOpponent()) {
            model.put("newOpponent", prepareProfile(getModel().getTeams().opponentTeam(team.getProfileId()).getProfile()));
        }
    }

    public void fillModel(Map<String, Object> model, RivalTeam team) {
        fillModelBasic(model, team);
        if (getModel().status == RivalStatus.ANSWERING) {
            fillModelAnswering(model, team);
        } else if (getModel().status == RivalStatus.ANSWERED) {
            model.put("task", getModel().taskDTOs.get(getModel().currentTaskIndex));
            fillModelAnswered(model, team);
        } else if (getModel().status == RivalStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model, team);
        } else if (getModel().status == RivalStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model, team);
            fillModelAnsweringTimeout(model, team);
        } else if (getModel().status == RivalStatus.CLOSED) {
            fillModelClosed(model, team);
        } else if (getModel().status == RivalStatus.CHOOSING_TASK_PROPS) {
            fillModelChoosingTaskProps(model, team);
        } else if (getModel().status == RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT) {
            fillModelChoosingTaskPropsTimeout(model);
        } else if (getModel().status == RivalStatus.CHOSEN_TASK_PROPS) {
            fillModelChosenTaskProps(model);
        } else if (getModel().status == RivalStatus.INTRO) {
            fillModelIntro(model, team);
        }
    }
}
