package com.ww.repository.outside.rival.season;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.model.entity.outside.rival.season.Season;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonGradeRepository extends CrudRepository<SeasonGrade, Long> {
    @Cacheable("seasonGradesByType")
    List<SeasonGrade> findAllByType(RivalType type);
}
