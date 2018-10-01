package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.*;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class RivalContainer {
    protected RivalType type;
    protected RivalImportance importance;
    protected Profile creatorProfile;
    protected Profile opponentProfile;

    protected Long creatorEloChange;
    protected Long opponentEloChange;

    protected final Map<Long, RivalProfileContainer> profileIdRivalProfileContainerMap = new HashMap<>();
    protected int currentTaskIndex = -1;

    protected CopyOnWriteArrayList<Question> questions = new CopyOnWriteArrayList<>();
    protected CopyOnWriteArrayList<TaskDTO> taskDTOs = new CopyOnWriteArrayList<>();

    protected Instant nextTaskDate;
    protected Instant endChoosingTaskPropsDate;
    protected Instant endAnsweringDate;

    protected Long answeredProfileId;
    protected Long markedAnswerId;

    protected Boolean draw;
    protected Profile winner;
    protected Profile looser;

    protected Boolean resigned;

    protected Category chosenCategory;
    protected DifficultyLevel chosenDifficulty;
    protected Boolean isChosenCategory;
    protected Boolean isChosenDifficulty;

    protected RivalStatus status = RivalStatus.OPEN;

    public void storeInformationFromInitContainer(RivalInitContainer container) {
        this.type = container.getType();
        this.importance = container.getImportance();
        this.creatorProfile = container.getCreatorProfile();
        this.opponentProfile = container.getOpponentProfile();
    }

    public boolean isOpponent() {
        return opponentProfile != null;
    }

    public void addProfile(Long id, RivalProfileContainer rivalProfileContainer) {
        profileIdRivalProfileContainerMap.put(id, rivalProfileContainer);
    }

    public RivalProfileContainer getRivalProfileContainer(Long id) {
        return profileIdRivalProfileContainerMap.get(id);
    }

    public RivalProfileContainer getOpponentRivalProfileContainer(Long id) {
        return getRivalProfileContainer(getRivalProfileContainer(id).getOpponentId());
    }

    public void setWinnerLooser(Profile winner) {
        this.draw = false;
        this.winner = winner;
        this.looser = getOpponentRivalProfileContainer(winner.getId()).getProfile();
    }

    public boolean isRanking() {
        return importance == RivalImportance.RANKING;
    }

    public Long findCorrectAnswerId(int taskIndex) {
        if (taskIndex >= questions.size()) {
            throw new IllegalArgumentException("taskIndex outside of questions");
        }
        return questions.get(taskIndex).getAnswers().stream().filter(Answer::getCorrect).findFirst().get().getId();
    }

    public Long findCurrentCorrectAnswerId() {
        return findCorrectAnswerId(currentTaskIndex);
    }

    public abstract String findChoosingTaskPropsTag();

    public abstract Optional<Profile> findWinner();

    public boolean randomChooseTaskProps() {
        return findChoosingTaskPropsTag() == null;
    }

    public int getCurrentTaskPoints() {
        return taskDTOs.get(currentTaskIndex).getPoints();
    }

    public void increaseCurrentTaskIndex() {
        currentTaskIndex++;
    }

    public boolean isReady() {
        return profileIdRivalProfileContainerMap.values().stream()
                .filter(rivalProfileContainer -> rivalProfileContainer.getStatus() != RivalProfileStatus.READY)
                .collect(Collectors.toList()).size() == 0;
    }

    public RivalProfileDTO prepareProfile(Long profileId) {
        return prepareProfile(getRivalProfileContainer(profileId).getProfile());
    }

    public RivalProfileDTO prepareProfile(Profile profile) {
        return new RivalProfileDTO(profile, type);
    }

    public void addTask(Question question, TaskDTO taskDTO) {
        questions.add(question);
        taskDTOs.add(taskDTO);
    }

    public void forEachProfile(Consumer<? super RivalProfileContainer> action) {
        profileIdRivalProfileContainerMap.values().parallelStream().forEach(action);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        model.put("status", status);
        model.put("importance", importance.name());
        model.put("type", type.name());
        model.put("profile", prepareProfile(rivalProfileContainer.getProfile()));
        if (isOpponent()) {
            model.put("opponent", prepareProfile(rivalProfileContainer.getOpponentId()));
        }
    }

    public void fillModelIntro(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        fillModelBasic(model, rivalProfileContainer);
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
        model.put("isDraw", draw);
        if (!draw) {
            model.put("winnerTag", winner.getTag());
        }
        model.put("resigned", resigned);
    }

    public void fillModelEloChanged(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        if (!isRanking()) {
            return;
        }
        model.put("newProfile", prepareProfile(rivalProfileContainer.getProfile()));
        if (isOpponent()) {
            model.put("newOpponent", prepareProfile(rivalProfileContainer.getOpponentId()));
        }
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
