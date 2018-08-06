package com.ww.model.entity.rival.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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
    private String animationContent;
    @Column(length = 4000)
    private String textContentPolish;
    @Column(length = 4000)
    private String textContentEnglish;
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false, updatable = false)
    private TaskType type;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers;

    public Question(TaskType type) {
        this.type = type;
    }

    public void setTextContent(String content) {
        this.setTextContentPolish(content);
        this.setTextContentEnglish(content);
    }

    public void initAnswerIds() {
        long id = 0;
        for (Answer answer : answers) {
            answer.setId(id++);
        }
    }
}
