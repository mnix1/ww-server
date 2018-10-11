package com.ww.repository.outside.rival.season;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.Season;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends CrudRepository<Season, Long> {
    Optional<Season> findFirstByTypeAndCloseDateIsNull(RivalType type);

    Optional<Season> findFirstByTypeOrderByStartDateDesc(RivalType type);
}
