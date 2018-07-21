package com.ww.database;

import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.rival.task.geography.GeographyCountryService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import com.ww.service.rival.task.music.MusicTrackService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;


@NoArgsConstructor
@Service
public class Init {

    @Autowired
    MusicTrackService musicTrackService;

    @Autowired
    GeographyCountryService geographyCountryService;

    @Autowired
    MemoryTaskHelperService memoryTaskHelperService;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    private Random random = new SecureRandom();

    public void init() {
        initMusicTracks();
        initGeographyCountries();
        memoryTaskHelperService.initShapes();
        memoryTaskHelperService.initColors();
    }

    public void initMusicTracks() {
        musicTrackService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html");
        musicTrackService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst");
        musicTrackService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst");
        musicTrackService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst");
        musicTrackService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst");
        musicTrackService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst");
    }

    public void initGeographyCountries() {
        geographyCountryService.loadAndDownloadResources();
    }


}
