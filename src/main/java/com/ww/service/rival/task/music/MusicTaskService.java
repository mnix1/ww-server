package com.ww.service.rival.task.music;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.MusicTrack;
import com.ww.repository.rival.task.category.MusicTrackRepository;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElementIndex;

@Service
public class MusicTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    MusicTrackRepository musicTrackRepository;

    private static String VERSE = "_V_";
    private static String LINE = "_L_";

    public Question generate(Language lang, MusicTaskType type) {
        List<MusicTrack> tracks = musicTrackRepository.findAllByLang(lang);
        MusicTrack track = randomElement(tracks);
        List<List<String>> verses = trackVerseLineContent(track.getContent());
        Map<String, Integer> allLinesRepeatMap = new HashMap<>();
        List<String> allLines = trackAllLineContent(verses);
        System.out.println("Track: " + track + " lines: " + allLines.size());
        allLines.forEach(line -> {
            if (allLinesRepeatMap.containsKey(line)) {
                Integer repeat = allLinesRepeatMap.get(line);
                allLinesRepeatMap.remove(line);
                allLinesRepeatMap.put(line, repeat + 1);
            } else {
                allLinesRepeatMap.put(line, 1);
            }
        });
        int questionLineIndex = randomElementIndex(allLines, 1, 1);
        String questionLine = allLines.get(questionLineIndex);
        while (allLinesRepeatMap.get(questionLine) > 1) {
            questionLineIndex = randomElementIndex(allLines, 1, 1);
            questionLine = allLines.get(questionLineIndex);
        }
        int correctAnswerIndex = 0;
        List<Integer> wrongAnswerIndexes = new ArrayList<>(3);
        if (type == MusicTaskType.NEXT_LINE) {
            correctAnswerIndex = questionLineIndex + 1;
        }
        if (type == MusicTaskType.PREVIOUS_LINE) {
            correctAnswerIndex = questionLineIndex - 1;
        }
        while (wrongAnswerIndexes.size() < 3) {
            int wrongAnswerIndex = randomElementIndex(allLines);
            String wrongAnswer = allLines.get(wrongAnswerIndex);
            if (wrongAnswerIndex != correctAnswerIndex && wrongAnswerIndex != questionLineIndex
                    && allLinesRepeatMap.get(wrongAnswer) == 1 && !wrongAnswerIndexes.contains(wrongAnswerIndex)) {
                wrongAnswerIndexes.add(wrongAnswerIndex);
            }
        }
        Question question = prepareQuestion(type, track, questionLine, lang);
        List<Answer> answers = prepareAnswers(allLines.get(correctAnswerIndex), wrongAnswerIndexes.stream().map(i -> allLines.get(i)).collect(Collectors.toList()));
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(MusicTaskType type, MusicTrack track, String questionLine, Language lang) {
        Question question = new Question();
        question.setCategory(Category.MUSIC);
        if (Language.addPolish(lang)) {
            String content = "W tekście utworu \"" + track.getName() + "\" zespołu " + track.getAuthor();
            if (type == MusicTaskType.NEXT_LINE) {
                content += " po wierszu: \"";
            }
            if (type == MusicTaskType.PREVIOUS_LINE) {
                content += " przed wierszem: \"";
            }
            content += questionLine + "\" występuje wiersz";
            question.setTextContentPolish(content);
        }
        if (Language.addEnglish(lang)) {
            // TODO add english
        }
        return question;
    }

    private List<Answer> prepareAnswers(String correctAnswerLine, List<String> wrongAnswerLines) {
        List<Answer> answers = new ArrayList<>();
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent(correctAnswerLine);
        answers.add(correctAnswer);
        answers.addAll(wrongAnswerLines.stream().map(s -> {
            Answer wrongAnswer = new Answer(false);
            wrongAnswer.setTextContent(s);
            return wrongAnswer;
        }).collect(Collectors.toList()));
        return answers;
    }

    private List<List<String>> trackVerseLineContent(String content) {
        List<String> verses = Arrays.asList(content.split(VERSE));
        int offset = verses.size() > 3 ? 3 : (verses.size() > 1 ? 1 : 0);
        return verses.stream()
                .limit(verses.size() - offset)
                .map(verse -> Arrays.asList(verse.split(LINE)).stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<String> trackAllLineContent(List<List<String>> verseLines) {
        return verseLines.stream().flatMap(strings -> strings.stream()).map(String::trim).collect(Collectors.toList());
    }

}
