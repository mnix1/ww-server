package com.ww.model.dto.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private Category category;
    private TaskDifficultyLevel taskDifficultyLevel;
    private TaskRenderer taskRenderer;
    private String imageContent;
    private String animationContent;
    private String textContentPolish;
    private String textContentEnglish;
    private List<AnswerDTO> answers;

    public TaskDTO(Question question) {
        this.id = question.getId();
        this.category = question.getType().getCategory();
        this.taskDifficultyLevel = question.getDifficultyLevel();
        this.taskRenderer = question.getType().getRenderer();
        this.imageContent = question.getImageContent();
        this.animationContent = question.getAnimationContent();
        this.textContentPolish = question.getTextContentPolish();
        this.textContentEnglish = question.getTextContentEnglish();
        this.answers = question.getAnswers().stream().map(AnswerDTO::new).collect(Collectors.toList());
    }
}
