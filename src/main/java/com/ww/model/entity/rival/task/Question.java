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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Category category;
    private TaskRenderer taskRenderer = TaskRenderer.TEXT;
    @Column(length = 4000)
    private String imageContent;
    @Column(length = 4000)
    private String animationContent;
    @Column(length = 4000)
    private String textContentPolish;
    @Column(length = 4000)
    private String textContentEnglish;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers;

    public void setTextContent(String content) {
        this.setTextContentPolish(content);
        this.setTextContentEnglish(content);
    }
}
