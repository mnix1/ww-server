package com.ww.model.dto.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private Category category;
    private DifficultyLevel difficultyLevel;
    private TaskRenderer questionRenderer;
    private TaskRenderer answerRenderer;
    private String imageContent;
    private String htmlContent;
    private String dateContent;
    private String animationContent;
    private String textContentPolish;
    private String textContentEnglish;
    private List<AnswerDTO> answers;
    private Integer points;

    public TaskDTO(Question question) {
        this.id = question.getId();
        this.category = question.getType().getCategory();
        this.difficultyLevel = question.getDifficultyLevel();
        this.questionRenderer = question.getType().getQuestionRenderer();
        this.answerRenderer = question.getType().getAnswerRenderer();
        this.imageContent = question.getImageContent();
        this.htmlContent = question.getHtmlContent();
        this.dateContent = question.getDateContent() != null ? question.getDateContent().toString() : null;
        this.animationContent = question.getAnimationContent();
        this.textContentPolish = question.getTextContentPolish();
        this.textContentEnglish = question.getTextContentEnglish();
        this.answers = question.getAnswers().stream().map(AnswerDTO::new).collect(Collectors.toList());
        this.points = this.difficultyLevel.getPoints();
    }

    public TaskDTO(TaskDTO taskDTO) {
        this.id = taskDTO.getId();
        this.category = taskDTO.getCategory();
        this.difficultyLevel = taskDTO.getDifficultyLevel();
        this.points = this.difficultyLevel.getPoints();
    }

    public TaskMetaDTO toTaskMeta(){
        return new TaskMetaDTO(this);
    }
}
