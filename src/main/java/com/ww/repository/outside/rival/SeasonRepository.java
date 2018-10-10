package com.ww.repository.outside.rival;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.Season;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends CrudRepository<Season, Long> {
    Optional<Season> findFirstByRivalTypeAndCloseDateIsNull(RivalType type);

    Optional<Season> findFirstByRivalTypeOrderByStartDateDesc(RivalType type);
}
