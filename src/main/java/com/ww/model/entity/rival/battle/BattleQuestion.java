package com.ww.model.entity.rival.battle;

import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BattleQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "battle_id", nullable = false, updatable = false)
    private Battle battle;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    public BattleQuestion(Battle battle, Question question) {
        this.battle = battle;
        this.question = question;
    }
}
