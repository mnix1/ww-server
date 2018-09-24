package com.ww.database;

import com.ww.repository.outside.social.ProfileRepository;
import com.ww.service.rival.task.country.CountryService;
import com.ww.service.rival.task.element.ElementService;
import com.ww.service.rival.task.lyrics.TrackService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import com.ww.service.rival.task.olympicgames.OlympicMedalService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Service
public class InitService {
    private static final Logger logger = LoggerFactory.getLogger(InitService.class);

    @Autowired
    private TrackService trackService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private OlympicMedalService olympicMedalService;

    @Autowired
    private MemoryTaskHelperService memoryTaskHelperService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private InitWisiesService initWisiesService;

    @Autowired
    private InitTaskTypesService initTaskTypesService;

    @Autowired
    private InitBooksService initBooksService;

    @Autowired
    private InitClipartsService initClipartsService;

    @Autowired
    private InitCampaignsService initCampaignsService;
    @Autowired
    private InitAutoService initAutoService;

    public void init() {
        if (profileRepository.findAll().size() == 0) {
            logger.debug("INITIALIZING initBooks");
            initBooksService.initBooks();
            logger.debug("INITIALIZING initTaskTypes");
            initTaskTypesService.initTaskTypes();
            logger.debug("INITIALIZING initWisies");
            initWisiesService.initWisies();
            logger.debug("INITIALIZING initCampaigns");
            initCampaignsService.initCampaigns();
            logger.debug("INITIALIZING initProfiles");
            initProfiles();
            logger.debug("INITIALIZING initMusicTracks");
            initMusicTracks();
            logger.debug("INITIALIZING initCliparts");
            initClipartsService.initCliparts();
            logger.debug("INITIALIZING initGeographyCountries");
            initGeographyCountries();
            logger.debug("INITIALIZING initChemistryElements");
            initChemistryElements();
            logger.debug("INITIALIZING initOlympicMedals");
            initOlympicMedals();
            logger.debug("INITIALIZING initShapes");
            memoryTaskHelperService.initShapes();
            logger.debug("INITIALIZING initColors");
            memoryTaskHelperService.initColors();
            logger.debug("INITIALIZING initAutos");
            initAutoService.initAutos();
        }
        logger.debug("ALL INITIALIZED");
    }

    public void initProfiles() {
//        List<Profile> profiles = new ArrayList<>();
//        profiles.add(new Profile(TagHelper.randomTag(), "Kozioł23"));
//        profiles.add(new Profile(TagHelper.randomTag(), "bocian"));
//        for (int i = 0; i < 100; i++) {
//            profiles.add(new Profile(TagHelper.randomTag(), "test" + i));
//        }
//        profileRepository.saveAll(profiles);
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

    public void initOlympicMedals() {
        olympicMedalService.loadResources();
    }


}
