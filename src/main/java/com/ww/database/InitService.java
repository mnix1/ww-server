package com.ww.database;

import com.ww.helper.TagHelper;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.BookRepository;
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
    private InitWisiesService initWisiesService;

    @Autowired
    private InitTaskTypesService initTaskTypesService;

    @Autowired
    private InitBooksService initBooksService;

    @Autowired
    private InitClipartsService initClipartsService;

    @Autowired
    private InitCampaignsService initCampaignsService;

    public void init() {
        initBooksService.initBooks();
        initTaskTypesService.initTaskTypes();
        initWisiesService.initWisies();
        initCampaignsService.initCampaigns();
        initProfiles();
        initMusicTracks();
        initClipartsService.initCliparts();
        initGeographyCountries();
        initChemistryElements();
        memoryTaskHelperService.initShapes();
        memoryTaskHelperService.initColors();
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


}
