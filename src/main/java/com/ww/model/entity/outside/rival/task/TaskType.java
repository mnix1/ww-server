package com.ww.model.entity.outside.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.wisie.WisdomAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TaskType {
    protected static final Logger logger = LoggerFactory.getLogger(TaskType.class);

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
        double sum = 0;
        Set<WisdomAttribute> attributes = new HashSet<>();
        for (TaskWisdomAttribute taskWisdomAttribute : wisdomAttributes) {
            sum += taskWisdomAttribute.getValue();
            attributes.add(taskWisdomAttribute.getWisdomAttribute());
        }
        if (sum > 1.01 || sum < 0.99 || attributes.size() != wisdomAttributes.size()) {
            logger.error("Wrong initialized values for category={}, value={}, sum={}", category, value, sum);
            throw new IllegalArgumentException();
        }
    }
}
