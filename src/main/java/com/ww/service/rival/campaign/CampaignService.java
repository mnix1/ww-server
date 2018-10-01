package com.ww.service.rival.campaign;

import com.ww.model.constant.book.BookType;
import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.dto.rival.campaign.ProfileCampaignDTO;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.rival.campaign.Campaign;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.repository.outside.rival.campaign.CampaignRepository;
import com.ww.repository.outside.rival.campaign.ProfileCampaignRepository;
import com.ww.repository.outside.wisie.ProfileCampaignWisieRepository;
import com.ww.service.book.BookService;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.RandomHelper.randomElement;

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
    private BookService bookService;

    @Autowired
    private ProfileBookService profileBookService;

    public List<CampaignDTO> list() {
        return campaignRepository.findAll().stream().map(CampaignDTO::new).collect(Collectors.toList());
    }

    public ProfileCampaignDTO activeDTO() {
        ProfileCampaign profileCampaign = active();
        if (profileCampaign == null) {
            return null;
        }
        return new ProfileCampaignDTO(profileCampaign);
    }

    public ProfileCampaign active() {
        return active(profileService.getProfileId());
    }

    public ProfileCampaign active(Long profileId) {
        return profileCampaignRepository.findOneByProfile_IdAndStatusNot(profileId, ProfileCampaignStatus.CLOSED);
    }

    public void setProfileCampaignWisies(ProfileCampaign profileCampaign) {
        profileCampaign.setWisies(profileCampaignWisieRepository.findAllByProfileCampaign_Id(profileCampaign.getId()));
    }

    public void save(ProfileCampaign profileCampaign) {
        profileCampaignRepository.save(profileCampaign);
        profileCampaignWisieRepository.saveAll(profileCampaign.getWisies());
    }

    @Transactional
    public Map<String, Object> init(CampaignType type, CampaignDestination destination, List<Long> ids) {
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
        if (!profile.hasEnoughResources(campaign.getCostResources())) {
            return putErrorCode(model);
        }
        if (active() != null) {
            return putErrorCode(model);
        }
        profile.subtractResources(campaign.getCostResources());
        ProfileCampaign profileCampaign = new ProfileCampaign(profile, campaign);
        for (Long id : ids) {
            ProfileWisie wisie = wisies.stream().filter(profileWisie -> profileWisie.getId().equals(id)).findFirst().get();
            ProfileCampaignWisie campaignWisie = new ProfileCampaignWisie(profileCampaign, wisie);
            profileCampaign.getWisies().add(campaignWisie);
        }
        profileCampaignRepository.save(profileCampaign);
        profileCampaignWisieRepository.saveAll(profileCampaign.getWisies());
        return putSuccessCode(model);
    }

    public BookType getBookGainForCampaign(Campaign campaign) {
        int campaignRating = campaign.getDifficultyLevel().getRating();
        List<BookType> bookTypes = bookService.findAll().stream()
                .filter(book -> book.getLevel() >= campaignRating)
                .map(Book::getType)
                .collect(Collectors.toList());
        return randomElement(bookTypes);
    }

    public synchronized Map<String, Object> close() {
        Map<String, Object> model = new HashMap<>();
        ProfileCampaign profileCampaign = active();
        if (profileCampaign == null || profileCampaign.getStatus() != ProfileCampaignStatus.FINISHED) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (profileCampaign.getBookGain() != null
                && profileBookService.isProfileBookShelfFull(profile.getId())) {
            return putCode(model, -2);
        }
        profileCampaign.setStatus(ProfileCampaignStatus.CLOSED);
        profileCampaign.setCloseDate(Instant.now());
        profileCampaignRepository.save(profileCampaign);
        if (profileCampaign.getBookGain() != null) {
            profileBookService.giveBook(profile, profileCampaign.getBookGain());
        }
        profile.addResources(profileCampaign.getGainResources());
        profileService.save(profile);
        return putSuccessCode(model);
    }

    public Optional<Campaign> findByTypeAndDestination(CampaignType type, CampaignDestination destination) {
        return campaignRepository.findAll().stream()
                .filter(campaign -> campaign.getType() == type && campaign.getDestination() == destination)
                .findFirst();
    }

}
