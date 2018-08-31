package com.ww.repository.hero;

import com.ww.model.entity.hero.ProfileHero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileHeroRepository extends CrudRepository<ProfileHero, Long> {
    List<ProfileHero> findAllByProfile_Id(Long profileId);
    List<ProfileHero> findAllByProfile_IdAndIdIn(Long profileId, List<Long> ids);
    Optional<ProfileHero> findByIdAndProfile_Id(Long id, Long profileId);
    List<ProfileHero> findAllByProfile_IdAndInTeam(Long profileId, Boolean inTeam);
}
