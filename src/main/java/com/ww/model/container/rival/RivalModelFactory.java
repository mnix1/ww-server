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
    protected RivalContainer container;

    public RivalProfileDTO prepareProfile(Long profileId) {
        return prepareProfile(container.getRivalProfileContainer(profileId).getProfile());
    }

    public RivalProfileDTO prepareProfile(Profile profile) {
        return new RivalProfileDTO(profile,container.type);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("importance", container.importance.name());
        model.put("type", container.type.name());
        model.put("profile", prepareProfile(rivalProfileContainer.getProfile()));
        if (container.isOpponent()) {
            model.put("opponent", prepareProfile(rivalProfileContainer.getOpponentId()));
        }
    }

    public void fillModelIntro(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        fillModelBasic(model, rivalProfileContainer);
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("task", container.taskDTOs.get(container.currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(container.nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("task", container.taskDTOs.get(container.currentTaskIndex));
        model.put("correctAnswerId", null);
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
        model.put("endAnsweringInterval", Math.max(container.endAnsweringDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("correctAnswerId", container.findCurrentCorrectAnswerId());
        model.put("markedAnswerId", container.markedAnswerId);
        model.put("meAnswered", container.answeredProfileId.equals(rivalProfileContainer.getProfileId()));
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("correctAnswerId", container.findCurrentCorrectAnswerId());
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
    }

    public void fillModelChoosingTaskPropsTimeout(Map<String, Object> model) {
        model.put("status", container.status);
        model.put("task", container.taskDTOs.get(container.currentTaskIndex).toTaskMeta());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("choosingTaskPropsInterval", Math.max(container.endChoosingTaskPropsDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("choosingTaskPropsTag", container.findChoosingTaskPropsTag());
        model.put("taskId", container.currentTaskIndex + 1);
        if (container.randomChooseTaskProps()) {
            model.put("task", container.taskDTOs.get(container.currentTaskIndex).toTaskMeta());
        } else {
            model.put("task", null);
            model.put("chosenCategory", container.chosenCategory);
            model.put("chosenDifficulty", container.chosenDifficulty);
            model.put("isChosenCategory", container.isChosenCategory);
            model.put("isChosenDifficulty", container.isChosenDifficulty);
        }
    }

    public void fillModelChosenTaskProps(Map<String, Object> model) {
        model.put("status", container.status);
        model.put("task", container.taskDTOs.get(container.currentTaskIndex).toTaskMeta());
    }

    public void fillModelClosed(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", container.status);
        model.put("isDraw", container.draw);
        if (!container.draw) {
            model.put("winnerTag", container.winner.getTag());
        }
        model.put("resigned", container.resigned);
    }

    public void fillModelEloChanged(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        if (!container.isRanking()) {
            return;
        }
        model.put("newProfile", prepareProfile(rivalProfileContainer.getProfile()));
        if (container.isOpponent()) {
            model.put("newOpponent", prepareProfile(rivalProfileContainer.getOpponentId()));
        }
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        fillModelBasic(model, rivalProfileContainer);
        if (container.status == RivalStatus.ANSWERING) {
            fillModelAnswering(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.ANSWERED) {
            model.put("task", container.taskDTOs.get(container.currentTaskIndex));
            fillModelAnswered(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model, rivalProfileContainer);
            fillModelAnsweringTimeout(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.CLOSED) {
            fillModelClosed(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.CHOOSING_TASK_PROPS) {
            fillModelChoosingTaskProps(model, rivalProfileContainer);
        } else if (container.status == RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT) {
            fillModelChoosingTaskPropsTimeout(model);
        } else if (container.status == RivalStatus.CHOSEN_TASK_PROPS) {
            fillModelChosenTaskProps(model);
        } else if (container.status == RivalStatus.INTRO) {
            fillModelIntro(model, rivalProfileContainer);
        }
    }
}
