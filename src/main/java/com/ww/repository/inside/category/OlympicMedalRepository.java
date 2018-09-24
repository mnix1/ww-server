package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.OlympicMedal;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OlympicMedalRepository extends CrudRepository<OlympicMedal, Long> {
    @Cacheable("allOlympicMedals")
    List<OlympicMedal> findAll();
    @Cacheable("olympicMedalsByTeam")
    List<OlympicMedal> findAllByTeam(Boolean team);
    @Cacheable("olympicMedalsByTeamAndCountryMapped")
    List<OlympicMedal> findAllByTeamAndCountryMapped(Boolean team, Boolean countryMapped);
    @Cacheable("olympicMedalsByOnlyTeamSport")
    List<OlympicMedal> findAllByPopularTeamSportAndCountryMapped(Boolean popularTeamSport, Boolean countryMapped);
    List<OlympicMedal> findAllByAthlete(String athlete);
    List<OlympicMedal> findAllByIocCountryCode(String iocCountryCode);
}
