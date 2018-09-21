package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.OlympicMedal;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
