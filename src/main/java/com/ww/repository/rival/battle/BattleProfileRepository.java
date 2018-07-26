package com.ww.repository.rival.battle;

import com.ww.model.entity.rival.battle.BattleProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleProfileRepository extends CrudRepository<BattleProfile, Long> {

}
