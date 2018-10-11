package com.ww.repository.outside.rival.season;

import com.ww.model.entity.outside.rival.season.ProfileSeason;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileSeasonRepository extends CrudRepository<ProfileSeason, Long> {
    List<ProfileSeason> findAllBySeason_IdOrderByEloDescPreviousEloDescUpdateDateAsc(Long seasonId);

    Optional<ProfileSeason> findFirstBySeason_IdAndProfile_Id(Long seasonId, Long profileId);
}
