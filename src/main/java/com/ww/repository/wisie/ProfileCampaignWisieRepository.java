package com.ww.repository.wisie;

import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileCampaignWisieRepository extends CrudRepository<ProfileCampaignWisie, Long> {
    List<ProfileCampaignWisie> findAllByProfileCampaign_Id(Long profileCampaignId);
}
