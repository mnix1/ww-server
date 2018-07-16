package com.ww.model.entity.rival.task;

import com.ww.model.constant.Category;
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
    private Category category;
    private String contentPolish;
    private String contentEnglish;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers;

    public void setContent(String content) {
        this.setContentPolish(content);
        this.setContentEnglish(content);
    }
}
