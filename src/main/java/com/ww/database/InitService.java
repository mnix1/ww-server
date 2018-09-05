package com.ww.database;

import com.ww.helper.TagHelper;
import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.entity.rival.campaign.Campaign;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.BookRepository;
import com.ww.repository.rival.campaign.CampaignRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.rival.task.country.CountryService;
import com.ww.service.rival.task.element.ElementService;
import com.ww.service.rival.task.lyrics.TrackService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Service
public class InitService {

    @Autowired
    private TrackService trackService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private MemoryTaskHelperService memoryTaskHelperService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private InitWisiesService initWisiesService;

    @Autowired
    private InitTaskTypesService initTaskTypesService;

    @Autowired
    private InitBooksService initBooksService;

    @Autowired
    private InitClipartsService initClipartsService;

    public void init() {
        if (bookRepository.findAll().size() == 0) {
            initBooksService.initBooks();
            initTaskTypesService.initTaskTypes();
            initWisiesService.initWisies();
            initCampaigns();
            initProfiles();
            initMusicTracks();
            initClipartsService.initCliparts();
            initGeographyCountries();
            initChemistryElements();
            memoryTaskHelperService.initShapes();
            memoryTaskHelperService.initColors();
        }
    }

    public void initProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(TagHelper.randomTag(), "Kozioł23", 3L));
        profiles.add(new Profile(TagHelper.randomTag(), "bocian", 10L));
        profileRepository.saveAll(profiles);
    }

    public void initCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.EASY, 0L, 0L, 0L, 0L, 0L, 5L, 10L, 1L));
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.NORMAL, 0L, 0L, 10L, 0L, 0L, 10L, 20L, 2L));
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.HARD, 0L, 0L, 20L, 0L, 0L, 20L, 40L, 4L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.EASY, 0L, 0L, 0L, 0L, 10L, 0L, 5L, 1L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.NORMAL, 10L, 0L, 0L, 0L, 20L, 0L, 10L, 2L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.HARD, 20L, 0L, 0L, 0L, 40L, 0L, 20L, 4L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.EASY, 0L, 0L, 0L, 0L, 5L, 10L, 0L, 1L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.NORMAL, 0L, 10L, 0L, 0L, 10L, 20L, 0L, 2L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.HARD, 0L, 20L, 0L, 0L, 20L, 40L, 0L, 4L));
        campaignRepository.saveAll(campaigns);
    }

    public void initMusicTracks() {
        trackService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html");
        trackService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst");
        trackService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst");
        trackService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst");
        trackService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst");
        trackService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst");
    }

    public void initGeographyCountries() {
        countryService.loadAndDownloadResources();
    }

    public void initChemistryElements() {
        elementService.loadResource();
    }


}
