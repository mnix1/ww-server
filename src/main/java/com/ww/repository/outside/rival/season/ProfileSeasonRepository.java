package com.ww.repository.outside.rival.season;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileSeasonRepository extends CrudRepository<ProfileSeason, Long> {
    List<ProfileSeason> findAllBySeason_IdOrderByEloDescPreviousEloDescUpdateDateAsc(Long seasonId);

    List<ProfileSeason> findAllBySeason_TypeAndProfile_IdOrderBySeason_OpenDateDesc(RivalType type, Long profileId);

    Optional<ProfileSeason> findFirstBySeason_IdAndProfile_Id(Long seasonId, Long profileId);

    List<ProfileSeason> findAllBySeason_CloseDateNotNullAndRewarded(Boolean rewarded);

    List<ProfileSeason> findAllBySeason_IdAndRewarded(Long seasonId, Boolean rewarded);
}
