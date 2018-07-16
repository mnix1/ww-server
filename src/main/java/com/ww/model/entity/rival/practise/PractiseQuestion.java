package com.ww.model.entity.rival.practise;

import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class PractiseQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "practise_id", nullable = false, updatable = false)
    private Practise practise;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;
}
