package com.ww.model.dto.task;

import com.ww.model.entity.rival.battle.Battle;
import com.ww.model.entity.rival.practise.Practise;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BattleDTO {

    private Long id;
    private List<QuestionDTO> questions;

    public BattleDTO(Battle battle, List<QuestionDTO> questions) {
        this.id = battle.getId();
        this.questions = questions;
    }
}
