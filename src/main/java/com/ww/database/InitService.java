package com.ww.database;

import com.ww.model.constant.Language;
import com.ww.service.rival.task.country.CountryService;
import com.ww.service.rival.task.element.ElementService;
import com.ww.service.rival.task.lyrics.TrackService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import com.ww.service.rival.task.olympicgames.OlympicMedalService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static com.ww.helper.EnvHelper.initOutsideDb;


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
    private InitGradeService initGradeService;

    @Autowired
    private InitAutoService initAutoService;

    @Autowired
    private Environment environment;

    public void initOutside() {
        if (initOutsideDb(environment)) {
            logger.debug("INITIALIZING initBooks");
            initBooksService.initBooks();
            logger.debug("INITIALIZING initTaskTypes");
            initTaskTypesService.initTaskTypes();
            logger.debug("INITIALIZING initWisies");
            initWisiesService.initWisies();
            logger.debug("INITIALIZING initCampaigns");
            initCampaignsService.initCampaigns();
            logger.debug("INITIALIZING initSeasonGrades");
            initGradeService.initSeasonGrades();
        }
    }

    public void init() {
        initOutside();

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

        logger.debug("ALL INITIALIZED");
    }

    public void initMusicTracks() {
        trackService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html", Language.POLISH);
        trackService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst", Language.POLISH);
        trackService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst", Language.POLISH);
        trackService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst", Language.POLISH);
        trackService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst", Language.POLISH);
        trackService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst", Language.POLISH);
        trackService.addTrack("Beata", "Upiłam się Tobą", "https://ising.pl/beata-upilam-sie-toba-tekst", Language.POLISH);
        trackService.addTrack("Szymon Wydra & Carpe Diem", "Życie jak poemat", "https://ising.pl/szymon-wydra-and-carpe-diem-zycie-jak-poemat-tekst", Language.POLISH);
        trackService.addTrack("Szymon Wydra & Carpe Diem", "Jak ja jej to powiem", "https://ising.pl/szymon-wydra-jak-ja-jej-to-powiem-tekst", Language.POLISH);
        trackService.addTrack("Dawid Podsiadło", "Nieznajomy", "https://ising.pl/dawid-podsiadlo-nieznajomy-tekst", Language.POLISH);
        trackService.addTrack("Dawid Podsiadło", "W dobrą stronę", "https://ising.pl/dawid-podsiadlo-w-dobra-strone-tekst", Language.POLISH);
        trackService.addTrack("Dawid Podsiadło", "Pastempomat", "https://ising.pl/dawid-podsiadlo-pastempomat-tekst", Language.POLISH);
        trackService.addTrack("Dawid Podsiadło", "Małomiasteczkowy", "https://ising.pl/dawid-podsiadlo-malomiasteczkowy-tekst", Language.POLISH);
        trackService.addTrack("Dawid Podsiadło", "Trójkąty i kwadraty", "https://ising.pl/dawid-podsiadlo-trojkaty-i-kwadraty-tekst", Language.POLISH);

        trackService.addTrack("The Beatles", "All You Need Is Love", "https://ising.pl/the-beatles-all-you-need-is-love-tekst", Language.ENGLISH);
        trackService.addTrack("The Beatles", "Hey Jude", "https://ising.pl/the-beatles-hey-jude-tekst", Language.ENGLISH);
        trackService.addTrack("The Beatles", "Yesterday", "https://ising.pl/the-beatles-yesterday-tekst", Language.ENGLISH);
        trackService.addTrack("Elvis Presley", "Love Me Tender", "https://ising.pl/elvis-presley-love-me-tender-tekst", Language.ENGLISH);
        trackService.addTrack("Eric Clapton", "Layla", "https://ising.pl/eric-clapton-layla-tekst", Language.ENGLISH);
        trackService.addTrack("Coldplay", "Hymn for the Weekend", "https://ising.pl/coldplay-hymn-for-the-weekend-tekst", Language.ENGLISH);
        trackService.addTrack("Coldplay", "Paradise", "https://ising.pl/coldplay-paradise-tekst", Language.ENGLISH);
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
