package com.ww.repository.rival.campaign;

import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCampaignRepository extends CrudRepository<ProfileCampaign, Long> {
    ProfileCampaign findOneByProfile_IdAndStatusNot(Long profileId, ProfileCampaignStatus status);
}
