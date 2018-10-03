package com.ww.repository.outside.rival.campaign;

import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileCampaignRepository extends CrudRepository<ProfileCampaign, Long> {
    Optional<ProfileCampaign> findOneByProfile_IdAndStatusNot(Long profileId, ProfileCampaignStatus status);
}
