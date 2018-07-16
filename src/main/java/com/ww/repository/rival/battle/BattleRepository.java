package com.ww.repository.rival.battle;

import com.ww.model.entity.rival.battle.Battle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends CrudRepository<Battle, Long> {

}
