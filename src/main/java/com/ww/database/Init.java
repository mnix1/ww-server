package com.ww.database;

import com.ww.helper.TagHelper;
import com.ww.model.constant.HeroType;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
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
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    HeroRepository heroRepository;

    private Random random = new SecureRandom();

    public void init() {
        initHeroes();
        initProfiles();
        initMusicTracks();
        initGeographyCountries();
        memoryTaskHelperService.initShapes();
        memoryTaskHelperService.initColors();
    }

    public void initProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(TagHelper.randomTag(), "Kozioł23", 3L));
        profiles.add(new Profile(TagHelper.randomTag(), "bocian", 10L));
        profileRepository.saveAll(profiles);
    }

    public void initMusicTracks() {
        musicTrackService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html");
        musicTrackService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst");
        musicTrackService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst");
        musicTrackService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst");
        musicTrackService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst");
        musicTrackService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst");
    }

    public void initHeroes() {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero("Wilku","Wolf", HeroType.WOLF));
        heroes.add(new Hero("Skorupny","Turtle", HeroType.TURTLE));
        heroes.add(new Hero("Tygrysor","Tiger", HeroType.TIGER));
        heroes.add(new Hero("Jadziuś","Snake", HeroType.SNAKE));
        heroes.add(new Hero("Smrodziak","Skunk", HeroType.SKUNK));
        heroes.add(new Hero("Wełnuś","Sheep", HeroType.SHEEP));
        heroes.add(new Hero("Ząbek","Shark", HeroType.SHARK));
        heroes.add(new Hero("Kicek","Rabbit", HeroType.RABBIT));
        heroes.add(new Hero("Misiaczek","Polar Bear", HeroType.POLAR_BEAR));
        heroes.add(new Hero("Zgapiacz","Parrot", HeroType.PARROT));
        heroes.add(new Hero("Pandziu","Panda", HeroType.PANDA_EAT));
        heroes.add(new Hero("Strusior","Ostrich", HeroType.OSTRICH));
        heroes.add(new Hero("Bujnogrzywy","Lion", HeroType.LION));
        heroes.add(new Hero("Skoczka","Kangaroo", HeroType.KANGAROO));
        heroes.add(new Hero("Rumaczek","Horse", HeroType.HORSE));
        heroes.add(new Hero("Gorylak","Gorilla", HeroType.GORILLA));
        heroes.add(new Hero("Lizuś","Fox", HeroType.FOX_MAN));
        heroes.add(new Hero("Lisiczka","Foxie", HeroType.FOX));
        heroes.add(new Hero("Trąbuś","Elephant", HeroType.ELEPHANT));
        heroes.add(new Hero("Orłowczak","Eagle", HeroType.EAGLE));
        heroes.add(new Hero("Grubełło","Fat Dragon", HeroType.DRAGON_FAT));
        heroes.add(new Hero("Smokełło","Blue Dragon", HeroType.DRAGON_BLUE));
        heroes.add(new Hero("Pierdołło","Green Dragon", HeroType.DRAGON));
        heroes.add(new Hero("Pulchny","Fat Dog", HeroType.DOG_FAT));
        heroes.add(new Hero("Bystruś","Idea Dog", HeroType.DOG));
        heroes.add(new Hero("Krokodylak","Crocodile", HeroType.CROCODILE));
        heroes.add(new Hero("Kicia","Cat Teacher", HeroType.CAT_TEACHER));
        heroes.add(new Hero("Kituś","Apple Cat", HeroType.CAT_PRESENTER));
        heroes.add(new Hero("Mruczka","Kitty", HeroType.CAT_BLUE));
        heroes.add(new Hero("Wielobłąd","Camel", HeroType.CAMEL));
        heroes.add(new Hero("Pudziuś","Bulldog", HeroType.BULLDOG));
        heroes.add(new Hero("Byku","Bull", HeroType.BULL));
        heroes.add(new Hero("Dźwiedzior","Bear", HeroType.BEAR));
        heroes.add(new Hero("Żądłolot","Bee", HeroType.BEE));
        heroes.add(new Hero("Żubrowar","Aurochs", HeroType.AUROCHS));
        heroes.add(new Hero("Mrówkacz","Ant", HeroType.ANT));
        heroRepository.saveAll(heroes);
    }

    public void initGeographyCountries() {
        geographyCountryService.loadAndDownloadResources();
    }


}
