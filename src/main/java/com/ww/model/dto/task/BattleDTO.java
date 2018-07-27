package com.ww.model.dto.task;

import com.ww.model.entity.rival.battle.Battle;
import com.ww.model.entity.rival.practise.Practise;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class BattleDTO {

    private Long id;
    private List<QuestionDTO> questions;
//    private Map<Long,Long> questionIdCorrectAnswerIdMap;

    public BattleDTO(Battle battle, List<QuestionDTO> questions) {
        this.id = battle.getId();
        this.questions = questions;
    }

//    public BattleDTO(Battle battle, List<QuestionDTO> questions, Map<Long,Long> questionIdCorrectAnswerIdMap) {
//        this.id = battle.getId();
//        this.questions = questions;
//        this.questionIdCorrectAnswerIdMap = questionIdCorrectAnswerIdMap;
//    }
}
