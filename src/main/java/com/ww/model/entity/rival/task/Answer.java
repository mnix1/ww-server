package com.ww.model.entity.rival.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String textContentPolish;
    private String textContentEnglish;
    private Boolean correct;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    public static Answer FALSE_EMPTY_ANSWER = new Answer(false);

    public Answer(Boolean correct) {
        this.correct = correct;
    }

    public void setTextContent(String content) {
        this.setTextContentPolish(content);
        this.setTextContentEnglish(content);
    }
}
