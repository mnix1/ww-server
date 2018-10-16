package com.ww.model.entity.outside.rival.task;

import com.ww.model.constant.rival.DifficultyLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 4000)
    private String imageContent;
    @Column(length = 4000)
    private String htmlContent;
    private Instant dateContent;
    @Column(length = 4000)
    private String animationContent;
    @Column(length = 4000)
    private String textContentPolish;
    @Column(length = 4000)
    private String textContentEnglish;
    private DifficultyLevel difficultyLevel;
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false, updatable = false)
    private TaskType type;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers;

    public Question(TaskType type) {
        this.type = type;
    }

    public Question(TaskType type, DifficultyLevel difficultyLevel) {
        this.type = type;
        this.difficultyLevel = difficultyLevel;
    }

    public void setTextContent(String content) {
        this.setTextContentPolish(content);
        this.setTextContentEnglish(content);
    }

    public String getTextContent() {
        if (getTextContentEnglish() != null) {
            return getTextContentEnglish();
        }
        return getTextContentPolish();
    }

    public void initAnswerIds() {
        long id = 0;
        for (Answer answer : answers) {
            answer.setId(id++);
        }
    }
}
