package com.ww.model.entity.rival.battle;

import com.ww.model.constant.rival.battle.BattleAnswerResult;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BattleAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BattleAnswerResult result;
    @ManyToOne
    @JoinColumn(name = "battle_id", nullable = false, updatable = false)
    private Battle battle;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

}
