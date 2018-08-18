package com.ww.database;

import com.ww.helper.TagHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.rival.task.type.*;
import com.ww.model.constant.shop.ChestType;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.shop.Chest;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.repository.rival.task.TaskTypeRepository;
import com.ww.repository.rival.task.TaskWisdomAttributeRepository;
import com.ww.repository.shop.ChestRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.rival.task.element.ElementService;
import com.ww.service.rival.task.country.CountryService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import com.ww.service.rival.task.lyrics.TrackService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;


@NoArgsConstructor
@Service
public class Init {

    @Autowired
    TaskTypeRepository taskTypeRepository;
    @Autowired
    TaskWisdomAttributeRepository taskWisdomAttributeRepository;

    @Autowired
    TrackService trackService;

    @Autowired
    CountryService countryService;

    @Autowired
    ElementService elementService;

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

    @Autowired
    ChestRepository chestRepository;

    private Random random = new SecureRandom();

    public void init() {
        initChests();
        initTaskTypes();
        initHeroes();
        initProfiles();
        initMusicTracks();
        initGeographyCountries();
        initChemistryElements();
        memoryTaskHelperService.initShapes();
        memoryTaskHelperService.initColors();
    }

    public void initTaskTypes() {
        List<TaskType> taskTypes = new ArrayList<>();
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.NEXT_LINE.name(), TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.PREVIOUS_LINE.name(), TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));

        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_SHAPE.name(), TaskRenderer.TEXT_ANIMATION, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));

        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MAX.name(), TaskRenderer.TEXT_ANSWERS_HTML, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MIN.name(), TaskRenderer.TEXT_ANSWERS_HTML, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.5)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.GCD.name(), TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.LCM.name(), TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.74),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.26)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.PRIME.name(), TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.94),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.06)
        ))));

        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.ADDITION.name(), TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.9),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MULTIPLICATION.name(), TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MODULO.name(), TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15)
        ))));

        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_ALPHA_2.name(), TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME.name(), TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_ALPHA_3.name(), TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME.name(), TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_POPULATION.name(), TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_POPULATION.name(), TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.25),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_AREA.name(), TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_AREA.name(), TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.6)
        ))));

        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MAX_ATOMIC_MASS.name(), TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MIN_ATOMIC_MASS.name(), TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SYMBOL.name(), TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NAME.name(), TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.8)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NUMBER_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_NUMBER.name(), TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NUMBER.name(), TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));

        List<TaskWisdomAttribute> wisdomAttributes = new ArrayList<>();
        for (TaskType taskType : taskTypes) {
            taskType.getWisdomAttributes().forEach(wisdomAttribute -> {
                wisdomAttribute.setType(taskType);
                wisdomAttributes.add(wisdomAttribute);
            });
        }
        taskTypeRepository.saveAll(taskTypes);
        taskWisdomAttributeRepository.saveAll(wisdomAttributes);
    }

    public void initProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(TagHelper.randomTag(), "Kozioł23", 3L));
        profiles.add(new Profile(TagHelper.randomTag(), "bocian", 10L));
        profileRepository.saveAll(profiles);
    }

    public void initMusicTracks() {
        trackService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html");
        trackService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst");
        trackService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst");
        trackService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst");
        trackService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst");
        trackService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst");
    }

    public void initChests() {
        List<Chest> chests = new ArrayList<>();
        chests.add(new Chest(ChestType.HERO));
        chestRepository.saveAll(chests);
    }

    public void initHeroes() {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero("Wilku", "Wolf", HeroType.WOLF, new HashSet<>(Arrays.asList(Category.COUNTRY, Category.MEMORY))));
        heroes.add(new Hero("Skorupny", "Turtle", HeroType.TURTLE, new HashSet<>(Arrays.asList(Category.MEMORY))));
        heroes.add(new Hero("Zdzigrys", "Tiger", HeroType.TIGER, new HashSet<>(Arrays.asList(Category.COUNTRY))));
        heroes.add(new Hero("Jadziuś", "Snake", HeroType.SNAKE, new HashSet<>(Arrays.asList(Category.LYRICS))));
        heroes.add(new Hero("Szopuś", "Raccoon", HeroType.RACCOON, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Wełnuś", "Sheep", HeroType.SHEEP, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Ząbek", "Shark", HeroType.SHARK, new HashSet<>(Arrays.asList(Category.EQUATION))));
        heroes.add(new Hero("Kicek", "Rabbit", HeroType.RABBIT, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Misiaczek", "Polar Bear", HeroType.POLAR_BEAR, new HashSet<>(Arrays.asList(Category.random(), Category.LYRICS))));
        heroes.add(new Hero("Zgapka", "Parrot", HeroType.PARROT, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Pandziu", "Panda", HeroType.PANDA_EAT, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Strusior", "Ostrich", HeroType.OSTRICH, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Bujnogrzyw", "Lion", HeroType.LION, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Skoczka", "Kangaroo", HeroType.KANGAROO, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Rumo", "Horse", HeroType.HORSE, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Goruś", "Gorilla", HeroType.GORILLA, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Lizuś", "Fox", HeroType.FOX_MAN, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Lisiczka", "Foxie", HeroType.FOX, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Trąbuś", "Elephant", HeroType.ELEPHANT, new HashSet<>(Arrays.asList(Category.EQUATION, Category.MEMORY))));
        heroes.add(new Hero("Orłuś", "Eagle", HeroType.EAGLE, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Grubełło", "Fat Dragon", HeroType.DRAGON_FAT, new HashSet<>(Arrays.asList(Category.COUNTRY, Category.EQUATION))));
        heroes.add(new Hero("Supełło", "Blue Dragon", HeroType.DRAGON_BLUE, new HashSet<>(Arrays.asList(Category.random(), Category.random()))));
        heroes.add(new Hero("Pikełło", "Green Dragon", HeroType.DRAGON, new HashSet<>(Arrays.asList(Category.random(), Category.random(), Category.random(), Category.random()))));
        heroes.add(new Hero("Pulszek", "Fat Dog", HeroType.DOG_FAT, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Bystruś", "Idea Dog", HeroType.DOG, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Kroczek", "Crocodile", HeroType.CROCODILE, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Kicia", "Cat Teacher", HeroType.CAT_TEACHER, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Kituś", "Apple Cat", HeroType.CAT_PRESENTER, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Mruczka", "Kitty", HeroType.CAT_BLUE, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Wielobłąd", "Camel", HeroType.CAMEL, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Pudziuś", "Bulldog", HeroType.BULLDOG, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Byku", "Bull", HeroType.BULL, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Dźwiedzior", "Bear", HeroType.BEAR, new HashSet<>(Arrays.asList(Category.random()))));
        heroes.add(new Hero("Żądłolot", "Bee", HeroType.BEE, new HashSet<>(Arrays.asList(Category.LYRICS))));
        heroes.add(new Hero("Żubrowar", "Aurochs", HeroType.AUROCHS, new HashSet<>(Arrays.asList(Category.MEMORY))));
        heroes.add(new Hero("Mrówkacz", "Ant", HeroType.ANT, new HashSet<>(Arrays.asList(Category.COUNTRY, Category.LYRICS))));
        heroRepository.saveAll(heroes);
    }

    public void initGeographyCountries() {
        countryService.loadAndDownloadResources();
    }

    public void initChemistryElements() {
        elementService.loadResource();
    }


}
