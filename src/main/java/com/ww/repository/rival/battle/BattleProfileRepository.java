package com.ww.repository.rival.battle;

import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.entity.rival.battle.BattleProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleProfileRepository extends CrudRepository<BattleProfile, Long> {
    List<BattleProfile> findAllByProfile_IdAndStatus(Long profileId, BattleProfileStatus status);
}
