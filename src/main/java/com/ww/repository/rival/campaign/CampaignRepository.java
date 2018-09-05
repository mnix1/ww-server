package com.ww.repository.rival.campaign;

import com.ww.model.entity.rival.campaign.Campaign;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends CrudRepository<Campaign, Long> {
    @Cacheable("allCampaigns")
    List<Campaign> findAll();
}
