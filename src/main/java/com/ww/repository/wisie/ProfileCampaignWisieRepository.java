package com.ww.repository.wisie;

import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileCampaignWisieRepository extends CrudRepository<ProfileCampaignWisie, Long> {
    List<ProfileCampaignWisie> findAllByProfile_Id(Long profileId);
    List<ProfileCampaignWisie> findAllByProfile_IdAndIdIn(Long profileId, List<Long> ids);
    Optional<ProfileCampaignWisie> findByIdAndProfile_Id(Long id, Long profileId);
    List<ProfileCampaignWisie> findAllByProfile_IdAndInTeam(Long profileId, Boolean inTeam);
}
