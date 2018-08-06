package com.ww.database;

import com.ww.helper.TagHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.rival.task.type.GeographyTaskType;
import com.ww.model.constant.rival.task.type.MathTaskType;
import com.ww.model.constant.rival.task.type.MemoryTaskType;
import com.ww.model.constant.rival.task.type.MusicTaskTypeValue;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.repository.rival.task.TaskTypeRepository;
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
    TaskTypeRepository taskTypeRepository;

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
        initTaskTypes();
        initHeroes();
        initProfiles();
        initMusicTracks();
        initGeographyCountries();
        memoryTaskHelperService.initShapes();
        memoryTaskHelperService.initColors();
    }

    public void initTaskTypes(){
        List<TaskType> taskTypes = new ArrayList<>();
        taskTypes.add(new TaskType(Category.MUSIC, MusicTaskTypeValue.NEXT_LINE.name(), TaskRenderer.TEXT, 1));
        taskTypes.add(new TaskType(Category.MUSIC, MusicTaskTypeValue.PREVIOUS_LINE.name(), TaskRenderer.TEXT, 4));

        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, 4));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, 3));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, 1));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, 4));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_SHAPE.name(), TaskRenderer.TEXT_ANIMATION, 3));

        taskTypes.add(new TaskType(Category.MATH, MathTaskType.ADDITION.name(), TaskRenderer.TEXT, 0));
        taskTypes.add(new TaskType(Category.MATH, MathTaskType.MULTIPLICATION.name(), TaskRenderer.TEXT, 1));
        taskTypes.add(new TaskType(Category.MATH, MathTaskType.MODULO.name(), TaskRenderer.TEXT, 1));

        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.COUNTRY_NAME_FROM_ALPHA_2.name(), TaskRenderer.TEXT, 1));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME.name(), TaskRenderer.TEXT, 2));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.COUNTRY_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE, 3));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.COUNTRY_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE, 1));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.CAPITAL_NAME_FROM_ALPHA_3.name(), TaskRenderer.TEXT, 0));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME.name(), TaskRenderer.TEXT, 2));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.CAPITAL_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE, 5));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.CAPITAL_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE, 4));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.MAX_POPULATION.name(), TaskRenderer.TEXT, 2));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.MIN_POPULATION.name(), TaskRenderer.TEXT, 2));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.MAX_AREA.name(), TaskRenderer.TEXT, 2));
        taskTypes.add(new TaskType(Category.GEOGRAPHY, GeographyTaskType.MIN_AREA.name(), TaskRenderer.TEXT, 2));

        taskTypeRepository.saveAll(taskTypes);
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
        heroes.add(new Hero("Zgapka","Parrot", HeroType.PARROT));
        heroes.add(new Hero("Pandziu","Panda", HeroType.PANDA_EAT));
        heroes.add(new Hero("Strusior","Ostrich", HeroType.OSTRICH));
        heroes.add(new Hero("Bujnogrzywy","Lion", HeroType.LION));
        heroes.add(new Hero("Skoczka","Kangaroo", HeroType.KANGAROO));
        heroes.add(new Hero("Rumaczek","Horse", HeroType.HORSE));
        heroes.add(new Hero("Gorylak","Gorilla", HeroType.GORILLA));
        heroes.add(new Hero("Lizuś","Fox", HeroType.FOX_MAN));
        heroes.add(new Hero("Lisiczka","Foxie", HeroType.FOX));
        heroes.add(new Hero("Trąbuś","Elephant", HeroType.ELEPHANT));
        heroes.add(new Hero("Orzełko","Eagle", HeroType.EAGLE));
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
