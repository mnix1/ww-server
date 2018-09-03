package com.ww.database;

import com.ww.helper.EloHelper;
import com.ww.helper.TagHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.rival.task.type.*;
import com.ww.model.constant.book.BookType;
import com.ww.model.entity.wisie.Wisie;
import com.ww.model.entity.rival.task.Clipart;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.social.Profile;
import com.ww.repository.wisie.WisieRepository;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.repository.rival.task.TaskTypeRepository;
import com.ww.repository.rival.task.TaskWisdomAttributeRepository;
import com.ww.repository.rival.task.category.ClipartRepository;
import com.ww.repository.book.BookRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.rival.task.country.CountryService;
import com.ww.service.rival.task.element.ElementService;
import com.ww.service.rival.task.lyrics.TrackService;
import com.ww.service.rival.task.memory.MemoryTaskHelperService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

import static com.ww.helper.ImageHelper.convertSvgToPng;


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
    WisieRepository wisieRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ClipartRepository clipartRepository;

    private Random random = new SecureRandom();

    public void init() {
        if (bookRepository.findAll().size() == 0) {
            initBooks();
            initTaskTypes();
            initWisies();
            initProfiles();
            initMusicTracks();
            initCliparts();
            initGeographyCountries();
            initChemistryElements();
            memoryTaskHelperService.initShapes();
            memoryTaskHelperService.initColors();
        }
    }

    public void initTaskTypes() {
        List<TaskType> taskTypes = new ArrayList<>();
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.NEXT_LINE.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.PREVIOUS_LINE.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));

        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_SHAPE.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));

        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MAX.name(), TaskRenderer.TEXT, TaskRenderer.EQUATION, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MIN.name(), TaskRenderer.TEXT, TaskRenderer.EQUATION, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.5)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.GCD.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.LCM.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.74),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.26)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.PRIME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.94),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.06)
        ))));

        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.ADDITION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.9),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MULTIPLICATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MODULO.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.FIND_X.name(), TaskRenderer.TEXT_EQUATION, TaskRenderer.EQUATION, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.95),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05)
        ))));

        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_ALPHA_2.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_ALPHA_3.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_POPULATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_POPULATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.25),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_AREA.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_AREA.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.6)
        ))));

        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MAX_ATOMIC_MASS.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MIN_ATOMIC_MASS.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SYMBOL.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.8)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NUMBER_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_NUMBER.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NUMBER.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));

        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.MISSING_CLIPART.name(), TaskRenderer.TEXT_IMAGE_PNG, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.09),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.41),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_CLIPART.name(), TaskRenderer.TEXT_IMAGE_PNG, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.79)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_DIFFERENCE_LEFT_MISSING.name(), TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.59),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_DIFFERENCE_RIGHT_MISSING.name(), TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.59),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));

        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.COLOR_MIXING.name(), TaskRenderer.TEXT_HTML, TaskRenderer.HTML, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.8)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_R.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_G.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_B.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_R.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_G.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_B.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));

        taskTypes.add(new TaskType(Category.TIME, TimeTaskType.CLOCK_ADD.name(), TaskRenderer.TEXT_DATE, TaskRenderer.DATE, 1, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.45),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.25)
        ))));
        taskTypes.add(new TaskType(Category.TIME, TimeTaskType.CLOCK_SUBTRACT.name(), TaskRenderer.TEXT_DATE, TaskRenderer.DATE, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.25),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.35)
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

    public void initCliparts() {
        List<Clipart> cliparts = new ArrayList<>();
        cliparts.add(new Clipart("pepper.svg", "Papryka", "Pepper"));
        cliparts.add(new Clipart("notebook.svg", "Notes", "Notebook"));
        cliparts.add(new Clipart("pickles.svg", "Ogórki konserwowe", "Pickles"));
        cliparts.add(new Clipart("scales.svg", "Waga", "Scales"));
        cliparts.add(new Clipart("cdDvd.svg", "Płyta CD/DVD", "CD/DVD"));
        cliparts.add(new Clipart("microscope.svg", "Mikroskop", "Microscope"));
        cliparts.add(new Clipart("basketball.svg", "Piłka do kosza", "Basketball ball"));
        cliparts.add(new Clipart("football.svg", "Piłka do nogi", "Football ball"));
        cliparts.add(new Clipart("fish.svg", "Ryba", "Fish"));
        cliparts.add(new Clipart("calculator.svg", "Kalkulator", "Calculator"));
        cliparts.add(new Clipart("hardDrive.svg", "Dysk twardy", "Hard drive"));
        cliparts.add(new Clipart("piano.svg", "Fortepian", "Piano"));
        cliparts.add(new Clipart("rubicCube.svg", "Kostka Rubika", "Rubic's cube"));
        cliparts.add(new Clipart("seat.svg", "Fotel", "Seat"));
        cliparts.add(new Clipart("car.svg", "Samochód", "Car"));
        cliparts.add(new Clipart("plumbersWrench.svg", "Klucz francuski", "Plumbers Wrench"));
        cliparts.add(new Clipart("thumbUp.svg", "Kciuk w górę", "Thumb up"));
        cliparts.add(new Clipart("thumbDown.svg", "Kciuk w dół", "Thumb down"));
        cliparts.add(new Clipart("gameController.svg", "Kontroler do gier", "Game controller"));
        cliparts.add(new Clipart("emoticon.svg", "Emotikona", "Emoticon"));
        cliparts.add(new Clipart("tree.svg", "Drzewo", "Tree"));
        cliparts.add(new Clipart("snowman.svg", "Bałwan", "Snowman"));
        cliparts.add(new Clipart("clouds.svg", "Chmury", "Clouds"));
        cliparts.add(new Clipart("podium.svg", "Podium", "Podium"));
        cliparts.add(new Clipart("medal.svg", "Medal", "Medal"));
        cliparts.add(new Clipart("speaker.svg", "Głośnik", "Speaker"));
        cliparts.add(new Clipart("helmet.svg", "Kask", "Helmet"));
        cliparts.add(new Clipart("scooter.svg", "Skuter", "Scooter"));
        cliparts.add(new Clipart("trashCan.svg", "Kosz na śmieci", "Trash can"));
        cliparts.add(new Clipart("balloon.svg", "Balon", "Balloon"));
        cliparts.add(new Clipart("aeroplan.svg", "Samolot", "Aeroplan"));
        cliparts.forEach(clipart -> convertSvgToPng(clipart.getResourcePath(), clipart.getPngResourcePath()));
        clipartRepository.saveAll(cliparts);
    }

    public void initBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(BookType.LEAFLET, 1, 0, 1L, 0L, 0L, true, false, 1L, 0L, "Gazetka sklepowa", "Leaflet"));
        books.add(new Book(BookType.FAIRY_TALE, 1, 0, 0L, 0L, 1L, true, false, 1L, 0L, "Bajka", "Fairy tale"));
        books.add(new Book(BookType.TV_GUIDE, 2, 1, 3L, 0L, 0L, true, false, 2L, 0L, "Program TV", "TV program"));
        books.add(new Book(BookType.COLORFUL_MAGAZINE, 2, 1, 2L, 0L, 1L, true, false, 2L, 0L, "Kolorowe czasopismo", "Colorful magazine"));
        books.add(new Book(BookType.SPORT_MAGAZINE, 2, 1, 1L, 0L, 2L, true, false, 2L, 0L, "Magazyn sportowy", "Sports magazine"));
        books.add(new Book(BookType.NEWSPAPER, 2, 1, 0L, 0L, 3L, true, false, 2L, 0L, "Gazeta", "Newspaper"));
        books.add(new Book(BookType.ROMANCE_NOVEL, 4, 2, 7L, 0L, 0L, true, false, 4L, 0L, "Romans", "Romance novel"));
        books.add(new Book(BookType.USER_MANUAL, 4, 2, 3L, 0L, 4L, true, false, 4L, 0L, "Instrukcja obsługi", "User manual"));
        books.add(new Book(BookType.BIOGRAPHY, 4, 2, 1L, 0L, 6L, true, false, 4L, 0L, "Biografia", "Biography"));
        books.add(new Book(BookType.HISTORICAL_NOVEL, 8, 3, 10L, 0L, 5L, true, false, 8L, 0L, "Powieść historyczna", "Historical novel"));
        books.add(new Book(BookType.CROSSWORD, 8, 3, 7L, 0L, 8L, true, false, 8L, 0L, "Krzyżówka", "Crossword"));
        books.add(new Book(BookType.FINANCIAL_STATEMENT, 8, 3, 1L, 0L, 14L, true, false, 8L, 0L, "Sprawozdanie finansowe", "Financial Statement"));
        books.add(new Book(BookType.WORLD_ATLAS, 12, 4, 10L, 0L, 13L, true, false, 12L, 0L, "Atlas świata", "World Atlas"));
        books.add(new Book(BookType.STUDENT_BOOK, 12, 4, 3L, 0L, 20L, true, false, 12L, 0L, "Podręcznik szkolny", "Student's book"));
        books.add(new Book(BookType.ENCYCLOPEDIA, 16, 5, 11L, 0L, 20L, true, false, 16L, 0L, "Encyklopedia", "Encyclopedia"));
        books.add(new Book(BookType.SCIENCE_ARTICLE, 16, 5, 5L, 0L, 26L, true, false, 16L, 0L, "Artykuł naukowy", "Science article"));
        books.add(new Book(BookType.MYSTERIOUS_BOOK, 20, 6, 0L, 30L, 9L, false, true, 0L, 100L, "Zagadkowa księga", "Mysterious  book"));
        books.add(new Book(BookType.SECRET_BOOK, 20, 6, 19L, 20L, 0L, false, false, 0L, 0L, "Tajemna księga", "Secret book"));
        bookRepository.saveAll(books);
    }

    public void initWisies() {
        List<Wisie> wisies = new ArrayList<>();
        wisies.add(new Wisie("Wilku", "Wolf", WisieType.WOLF));
        wisies.add(new Wisie("Skorupny", "Turtle", WisieType.TURTLE));
        wisies.add(new Wisie("Zdzigrys", "Tiger", WisieType.TIGER));
        wisies.add(new Wisie("Jaduś", "Snake", WisieType.SNAKE));
        wisies.add(new Wisie("Szopuś", "Raccoon", WisieType.RACCOON));
        wisies.add(new Wisie("Wełnuś", "Sheep", WisieType.SHEEP));
        wisies.add(new Wisie("Ząbek", "Shark", WisieType.SHARK));
        wisies.add(new Wisie("Kicek", "Rabbit", WisieType.RABBIT));
        wisies.add(new Wisie("Misiaczek", "Polar Bear", WisieType.POLAR_BEAR));
        wisies.add(new Wisie("Zgapka", "Parrot", WisieType.PARROT));
        wisies.add(new Wisie("Pandziu", "Panda", WisieType.PANDA_EAT));
        wisies.add(new Wisie("Strusior", "Ostrich", WisieType.OSTRICH));
        wisies.add(new Wisie("Bujnogrzyw", "Lion", WisieType.LION));
        wisies.add(new Wisie("Skoczka", "Kangaroo", WisieType.KANGAROO));
        wisies.add(new Wisie("Rumo", "Horse", WisieType.HORSE));
        wisies.add(new Wisie("Goruś", "Gorilla", WisieType.GORILLA));
        wisies.add(new Wisie("Lizuś", "Fox", WisieType.FOX_MAN));
        wisies.add(new Wisie("Lisiczka", "Foxie", WisieType.FOX));
        wisies.add(new Wisie("Trąbcia", "Elephant", WisieType.ELEPHANT));
        wisies.add(new Wisie("Orłuś", "Eagle", WisieType.EAGLE));
        wisies.add(new Wisie("Grubełło", "Fat Dragon", WisieType.DRAGON_FAT));
        wisies.add(new Wisie("Supełło", "Blue Dragon", WisieType.DRAGON_BLUE));
        wisies.add(new Wisie("Pikełło", "Green Dragon", WisieType.DRAGON));
        wisies.add(new Wisie("Pulszek", "Fat Dog", WisieType.DOG_FAT));
        wisies.add(new Wisie("Bystruś", "Idea Dog", WisieType.DOG));
        wisies.add(new Wisie("Kroczek", "Crocodile", WisieType.CROCODILE));
        wisies.add(new Wisie("Kicia", "Cat Teacher", WisieType.CAT_TEACHER));
        wisies.add(new Wisie("Kituś", "Apple Cat", WisieType.CAT_PRESENTER));
        wisies.add(new Wisie("Mruczka", "Kitty", WisieType.CAT_BLUE));
        wisies.add(new Wisie("Wielobłąd", "Camel", WisieType.CAMEL));
        wisies.add(new Wisie("Pudziuś", "Bulldog", WisieType.BULLDOG));
        wisies.add(new Wisie("Byku", "Bull", WisieType.BULL));
        wisies.add(new Wisie("Dźwiedzior", "Bear", WisieType.BEAR));
        wisies.add(new Wisie("Żądłolot", "Bee", WisieType.BEE));
        wisies.add(new Wisie("Żubrowar", "Aurochs", WisieType.AUROCHS));
        wisies.add(new Wisie("Mrówkacz", "Ant", WisieType.ANT));
        wisies.add(new Wisie("Pardzio", "Lampard", WisieType.LAMPARD));
        wisies.add(new Wisie("Smakełło", "Red Dragon", WisieType.DRAGON_RED));
        wisies.add(new Wisie("Słodzik", "Sweet Dog", WisieType.DOG_SWEET));
        wisies.add(new Wisie("Sowcia", "Owl", WisieType.OWL));
        wisies.add(new Wisie("Żabcia", "Frog", WisieType.FROG));
        wisies.add(new Wisie("Wiewcia", "Squirrel", WisieType.SQUIRREL));
        wisies.add(new Wisie("Pinguś", "Penguin", WisieType.PENGUIN));
        wisies.add(new Wisie("Morsu", "Walrus", WisieType.WALRUS));
        wisieRepository.saveAll(wisies);
    }

    public void initGeographyCountries() {
        countryService.loadAndDownloadResources();
    }

    public void initChemistryElements() {
        elementService.loadResource();
    }


}
