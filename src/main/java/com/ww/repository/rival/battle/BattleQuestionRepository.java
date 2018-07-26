package com.ww.repository.rival.battle;

import com.ww.model.entity.rival.battle.BattleQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleQuestionRepository extends CrudRepository<BattleQuestion, Long> {

}
