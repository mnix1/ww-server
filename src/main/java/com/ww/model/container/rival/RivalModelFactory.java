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
    public abstract RivalContainer getContainer();

    public RivalProfileDTO prepareProfile(Profile profile) {
        return new RivalProfileDTO(profile, getContainer().type);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("importance", getContainer().importance.name());
        model.put("type", getContainer().type.name());
        model.put("profile", prepareProfile(profileContainer.getProfile()));
        if (getContainer().isOpponent()) {
            model.put("opponent", prepareProfile(getContainer().getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getProfile()));
        }
    }

    public void fillModelIntro(Map<String, Object> model, RivalProfileContainer profileContainer) {
        fillModelBasic(model, profileContainer);
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(getContainer().nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex));
        model.put("correctAnswerId", null);
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
        model.put("endAnsweringInterval", Math.max(getContainer().endAnsweringDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("correctAnswerId", getContainer().findCurrentCorrectAnswerId());
        model.put("markedAnswerId", getContainer().markedAnswerId);
        model.put("meAnswered", getContainer().answeredProfileId.equals(profileContainer.getProfileId()));
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("correctAnswerId", getContainer().findCurrentCorrectAnswerId());
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
    }

    public void fillModelChoosingTaskPropsTimeout(Map<String, Object> model) {
        model.put("status", getContainer().status);
        model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex).toTaskMeta());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("choosingTaskPropsInterval", Math.max(getContainer().endChoosingTaskPropsDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("choosingTaskPropsTag", getContainer().findChoosingTaskPropsTag());
        model.put("taskId", getContainer().currentTaskIndex + 1);
        if (getContainer().randomChooseTaskProps()) {
            model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex).toTaskMeta());
        } else {
            model.put("task", null);
            model.put("chosenCategory", getContainer().chosenCategory);
            model.put("chosenDifficulty", getContainer().chosenDifficulty);
            model.put("isChosenCategory", getContainer().isChosenCategory);
            model.put("isChosenDifficulty", getContainer().isChosenDifficulty);
        }
    }

    public void fillModelChosenTaskProps(Map<String, Object> model) {
        model.put("status", getContainer().status);
        model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex).toTaskMeta());
    }

    public void fillModelClosed(Map<String, Object> model, RivalProfileContainer profileContainer) {
        model.put("status", getContainer().status);
        model.put("isDraw", getContainer().draw);
        if (!getContainer().draw) {
            model.put("winnerTag", getContainer().winner.getTag());
        }
        model.put("resigned", getContainer().resigned);
    }

    public void fillModelEloChanged(Map<String, Object> model, RivalProfileContainer profileContainer) {
        if (!getContainer().isRanking()) {
            return;
        }
        model.put("newProfile", prepareProfile(profileContainer.getProfile()));
        if (getContainer().isOpponent()) {
            model.put("newOpponent", prepareProfile(getContainer().getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId()).getProfile()));
        }
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer profileContainer) {
        fillModelBasic(model, profileContainer);
        if (getContainer().status == RivalStatus.ANSWERING) {
            fillModelAnswering(model, profileContainer);
        } else if (getContainer().status == RivalStatus.ANSWERED) {
            model.put("task", getContainer().taskDTOs.get(getContainer().currentTaskIndex));
            fillModelAnswered(model, profileContainer);
        } else if (getContainer().status == RivalStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model, profileContainer);
        } else if (getContainer().status == RivalStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model, profileContainer);
            fillModelAnsweringTimeout(model, profileContainer);
        } else if (getContainer().status == RivalStatus.CLOSED) {
            fillModelClosed(model, profileContainer);
        } else if (getContainer().status == RivalStatus.CHOOSING_TASK_PROPS) {
            fillModelChoosingTaskProps(model, profileContainer);
        } else if (getContainer().status == RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT) {
            fillModelChoosingTaskPropsTimeout(model);
        } else if (getContainer().status == RivalStatus.CHOSEN_TASK_PROPS) {
            fillModelChosenTaskProps(model);
        } else if (getContainer().status == RivalStatus.INTRO) {
            fillModelIntro(model, profileContainer);
        }
    }
}
