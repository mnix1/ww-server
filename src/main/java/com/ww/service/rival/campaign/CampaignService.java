package com.ww.service.rival.campaign;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.dto.rival.campaign.ProfileCampaignDTO;
import com.ww.model.entity.rival.campaign.Campaign;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.repository.rival.campaign.CampaignRepository;
import com.ww.repository.rival.campaign.ProfileCampaignRepository;
import com.ww.repository.wisie.ProfileCampaignWisieRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProfileCampaignRepository profileCampaignRepository;

    @Autowired
    private ProfileCampaignWisieRepository profileCampaignWisieRepository;

    @Autowired
    private ProfileWisieService profileWisieService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionService sessionService;

    public List<CampaignDTO> list() {
        return campaignRepository.findAll().stream().map(CampaignDTO::new).collect(Collectors.toList());
    }

    public ProfileCampaignDTO active() {
        ProfileCampaign profileCampaign = profileCampaignRepository.findOneByProfile_IdAndStatus(sessionService.getProfileId(), ProfileCampaignStatus.IN_PROGRESS);
        if (profileCampaign == null) {
            return null;
        }
        return new ProfileCampaignDTO(profileCampaign);
    }

    public synchronized Map<String, Object> init(CampaignType type, CampaignDestination destination, Set<Long> ids) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileWisie> wisies = profileWisieService.findByIds(ids);
        if (wisies == null) {
            return putErrorCode(model);
        }
        Optional<Campaign> optionalCampaign = findByTypeAndDestination(type, destination);
        if (!optionalCampaign.isPresent()) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        Campaign campaign = optionalCampaign.get();
        if (!profile.hasEnoughResources(campaign.getGoldCost(), campaign.getCrystalCost(), campaign.getWisdomCost(), campaign.getElixirCost())) {
            return putErrorCode(model);
        }
        if (active() != null) {
            return putErrorCode(model);
        }
        profile.changeResources(campaign.getGoldCost(), campaign.getCrystalCost(), campaign.getWisdomCost(), campaign.getElixirCost());
        ProfileCampaign profileCampaign = new ProfileCampaign(profile, campaign);
        for (ProfileWisie wisie : wisies) {
            ProfileCampaignWisie campaignWisie = new ProfileCampaignWisie(profileCampaign, wisie);
            profileCampaign.getWisies().add(campaignWisie);
        }
        profileCampaignRepository.save(profileCampaign);
        profileCampaignWisieRepository.saveAll(profileCampaign.getWisies());
        return putSuccessCode(model);
    }

    public Optional<Campaign> findByTypeAndDestination(CampaignType type, CampaignDestination destination) {
        return campaignRepository.findAll().stream()
                .filter(campaign -> campaign.getType() == type && campaign.getDestination() == destination)
                .findFirst();
    }

}
