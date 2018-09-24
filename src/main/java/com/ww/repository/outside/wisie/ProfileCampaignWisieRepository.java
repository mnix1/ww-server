package com.ww.repository.outside.wisie;

import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileCampaignWisieRepository extends CrudRepository<ProfileCampaignWisie, Long> {
    List<ProfileCampaignWisie> findAllByProfileCampaign_Id(Long profileCampaignId);
}
