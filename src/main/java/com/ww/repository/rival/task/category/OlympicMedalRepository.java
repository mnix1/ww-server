package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.OlympicMedal;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OlympicMedalRepository extends CrudRepository<OlympicMedal, Long> {
    @Cacheable("allOlympicMedals")
    List<OlympicMedal> findAll();
}
