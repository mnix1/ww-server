package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalProfileStatus;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class RivalContainer {
    protected final Map<Long, RivalProfileContainer> profileIdRivalProfileContainerMap = new HashMap<>();
    protected int currentTaskIndex = 0;

    protected List<Question> questions = new ArrayList<>();
    protected List<TaskDTO> taskDTOs = new ArrayList<>();

    protected Instant nextTaskDate;
    protected Instant endChoosingTaskPropsDate;
    protected Instant endAnsweringDate;

    protected Long answeredProfileId;
    protected Long markedAnswerId;

    protected String winnerTag;
    protected Boolean resigned;

    protected Category chosenCategory;
    protected TaskDifficultyLevel chosenDifficulty;
    protected Boolean isChosenCategory;
    protected Boolean isChosenDifficulty;

    protected RivalStatus status = RivalStatus.OPEN;

    public void addProfile(Long id, RivalProfileContainer rivalProfileContainer) {
        this.profileIdRivalProfileContainerMap.put(id, rivalProfileContainer);
    }

    public RivalProfileContainer getRivalProfileContainer(Long id) {
        return this.profileIdRivalProfileContainerMap.get(id);
    }

    public void setWinner(Long winnerId) {
        Profile profile = profileIdRivalProfileContainerMap.get(winnerId).getProfile();
        winnerTag = profile.getTag();
    }

    public Long findCorrectAnswerId(int taskIndex) {
        return questions.get(taskIndex).getAnswers().stream().filter(Answer::getCorrect).findFirst().get().getId();
    }

    public Long findCurrentCorrectAnswerId() {
        return findCorrectAnswerId(currentTaskIndex);
    }

    public abstract String findChoosingTaskPropsTag();

    public abstract String findWinnerTag();

    public boolean randomChooseTaskProps() {
        return findChoosingTaskPropsTag() == null;
    }

    public int getCurrentTaskPoints() {
        return taskDTOs.get(currentTaskIndex).getPoints();
    }

    public void increaseCurrentTaskIndex() {
        currentTaskIndex++;
    }

    public void profileReady(Long profileId) {
        getRivalProfileContainer(profileId).setStatus(RivalProfileStatus.READY);
    }

    public boolean isReady() {
        return profileIdRivalProfileContainerMap.values().stream()
                .filter(rivalProfileContainer -> rivalProfileContainer.getStatus() != RivalProfileStatus.READY)
                .collect(Collectors.toList()).size() == 0;
    }

    public ProfileDTO prepareProfile(Long profileId) {
        return new ProfileDTO(getRivalProfileContainer(profileId).getProfile());
    }

    public void addTask(Question question, TaskDTO taskDTO) {
        questions.add(question);
        taskDTOs.add(taskDTO);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("opponent", prepareProfile(rivalProfileContainer.getOpponentId()));
    }

    public void fillModelIntro(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        fillModelBasic(model, rivalProfileContainer);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex));
        model.put("correctAnswerId", null);
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
        model.put("endAnsweringInterval", Math.max(endAnsweringDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("correctAnswerId", findCorrectAnswerId(currentTaskIndex));
        model.put("markedAnswerId", markedAnswerId);
        model.put("meAnswered", answeredProfileId.equals(rivalProfileContainer.getProfileId()));
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("correctAnswerId", findCorrectAnswerId(currentTaskIndex));
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
//        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelChoosingTaskPropsTimeout(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("choosingTaskPropsInterval", Math.max(endChoosingTaskPropsDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("choosingTaskPropsTag", findChoosingTaskPropsTag());
        model.put("taskId", currentTaskIndex + 1);
        if (randomChooseTaskProps()) {
            model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        } else {
            model.put("task", null);
            model.put("chosenCategory", chosenCategory);
            model.put("chosenDifficulty", chosenDifficulty);
            model.put("isChosenCategory", isChosenCategory);
            model.put("isChosenDifficulty", isChosenDifficulty);
        }
    }

    public void fillModelChosenTaskProps(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelClosed(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("winnerTag", winnerTag);
        model.put("resigned", resigned);
    }

    public void forEachProfile(Consumer<? super RivalProfileContainer> action) {
        profileIdRivalProfileContainerMap.values().parallelStream().forEach(action);
    }


    public void fillModel(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        fillModelBasic(model, rivalProfileContainer);
        if (status == RivalStatus.ANSWERING) {
            fillModelAnswering(model, rivalProfileContainer);
        } else if (status == RivalStatus.ANSWERED) {
            model.put("task", taskDTOs.get(currentTaskIndex));
            fillModelAnswered(model, rivalProfileContainer);
        } else if (status == RivalStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model, rivalProfileContainer);
        } else if (status == RivalStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model, rivalProfileContainer);
            fillModelAnsweringTimeout(model, rivalProfileContainer);
        } else if (status == RivalStatus.CLOSED) {
            fillModelClosed(model, rivalProfileContainer);
        } else if (status == RivalStatus.CHOOSING_TASK_PROPS) {
            fillModelChoosingTaskProps(model, rivalProfileContainer);
        } else if (status == RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT) {
            fillModelChoosingTaskPropsTimeout(model);
        } else if (status == RivalStatus.CHOSEN_TASK_PROPS) {
            fillModelChosenTaskProps(model);
        } else if (status == RivalStatus.INTRO) {
            fillModelIntro(model, rivalProfileContainer);
        }
    }
}
