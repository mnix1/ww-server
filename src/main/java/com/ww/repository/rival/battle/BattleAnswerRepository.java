package com.ww.repository.rival.battle;

import com.ww.model.entity.rival.battle.BattleAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleAnswerRepository extends CrudRepository<BattleAnswer, Long> {

}
