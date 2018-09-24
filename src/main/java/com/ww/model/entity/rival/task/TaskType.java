package com.ww.model.entity.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskRenderer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Category category;
    private String value;
    private TaskRenderer questionRenderer;
    private TaskRenderer answerRenderer;
    private Integer difficulty;
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<TaskWisdomAttribute> wisdomAttributes;

    public TaskType(Category category, String value, TaskRenderer questionRenderer, TaskRenderer answerRenderer, Integer difficulty, Set<TaskWisdomAttribute> wisdomAttributes) {
        this.category = category;
        this.value = value;
        this.questionRenderer = questionRenderer;
        this.answerRenderer = answerRenderer;
        this.difficulty = difficulty * 10;
        this.wisdomAttributes = wisdomAttributes;
    }
}
