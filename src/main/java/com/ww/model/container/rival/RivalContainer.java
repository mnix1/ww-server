package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.*;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.container.rival.init.RivalOnePlayerInitContainer;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public abstract class RivalContainer {
    protected RivalType type;
    protected RivalImportance importance;
    protected Profile creatorProfile;
    protected Profile opponentProfile;

    protected Long creatorEloChange;
    protected Long opponentEloChange;

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

    public abstract RivalTeamsContainer getTeamsContainer();

    protected RivalContainer(RivalInitContainer container, RivalTeamsContainer teamsContainer) {
        this.type = container.getType();
        this.importance = container.getImportance();
        if (container.getPlayer() == RivalPlayer.TWO) {
            RivalTwoPlayerInitContainer c = (RivalTwoPlayerInitContainer) container;
            this.creatorProfile = c.getCreatorProfile();
            this.opponentProfile = c.getOpponentProfile();
            teamsContainer.getOpponents().put(creatorProfile.getId(), opponentProfile.getId());
            teamsContainer.getOpponents().put(opponentProfile.getId(), creatorProfile.getId());
        } else if (container.getPlayer() == RivalPlayer.ONE) {
            RivalOnePlayerInitContainer c = (RivalOnePlayerInitContainer) container;
            this.creatorProfile = c.getCreatorProfile();
        }
    }

    public boolean isOpponent() {
        return opponentProfile != null;
    }

    public void setWinnerLooser(Profile winner) {
        this.draw = false;
        this.winner = winner;
        this.looser = getTeamsContainer().opponentProfileContainer(winner.getId()).getProfile();
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

    public void addTask(Question question, TaskDTO taskDTO) {
        questions.add(question);
        taskDTOs.add(taskDTO);
    }
//
//    public void forEachProfile(Consumer<? super RivalProfileContainer> action) {
//        profileContainers.values().parallelStream().forEach(action);
//    }
}
