package com.ww.service.rival.task.lyrics;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.LyricsTaskTypeValue;
import com.ww.model.entity.inside.task.Track;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.inside.category.TrackRepository;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElementIndex;

@Service
public class LyricsTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    TrackRepository trackRepository;

    private static String VERSE = "_V_";
    private static String LINE = "_L_";

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, Language language) {
        LyricsTaskTypeValue typeValue = LyricsTaskTypeValue.valueOf(type.getValue());
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = Math.min(6, DifficultyLevel.answersCount(remainedDifficulty));
        if (language != Language.POLISH) {
            language = Language.ENGLISH;
        }
        List<Track> tracks = trackRepository.findAllByLanguage(language);
        Track track = randomElement(tracks);
        List<List<String>> verses = trackVerseLineContent(track.getContent());
        Map<String, Integer> allLinesRepeatMap = new HashMap<>();
        List<String> allLines = trackAllLineContent(verses);
//        System.out.println("Track: " + track + " lines: " + allLines.size());
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
        List<Integer> wrongAnswerIndexes = new ArrayList<>(answersCount - 1);
        if (typeValue == LyricsTaskTypeValue.NEXT_LINE) {
            correctAnswerIndex = questionLineIndex + 1;
        }
        if (typeValue == LyricsTaskTypeValue.PREVIOUS_LINE) {
            correctAnswerIndex = questionLineIndex - 1;
        }
        while (wrongAnswerIndexes.size() < answersCount - 1) {
            int wrongAnswerIndex = randomElementIndex(allLines);
            String wrongAnswer = allLines.get(wrongAnswerIndex);
            if (wrongAnswerIndex != correctAnswerIndex && wrongAnswerIndex != questionLineIndex
                    && allLinesRepeatMap.get(wrongAnswer) == 1 && !wrongAnswerIndexes.contains(wrongAnswerIndex)) {
                wrongAnswerIndexes.add(wrongAnswerIndex);
            }
        }
        Question question = prepareQuestion(type, difficultyLevel, typeValue, track, questionLine);
        List<Answer> answers = prepareAnswers(allLines.get(correctAnswerIndex), wrongAnswerIndexes.stream().map(i -> allLines.get(i)).collect(Collectors.toList()));
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, LyricsTaskTypeValue typeValue, Track track, String questionLine) {
        Question question = new Question(type, difficultyLevel);
        question.setTextContentPolish(getContentPolishQuestion(typeValue, track, questionLine));
        question.setTextContentEnglish(getContentEnglishQuestion(typeValue, track, questionLine));
        return question;
    }

    private String getContentPolishQuestion(LyricsTaskTypeValue typeValue, Track track, String questionLine) {
        String content = "W utworze \"" + track.getAuthor() + "\" - \"" + track.getName() + "\"";
        if (typeValue == LyricsTaskTypeValue.NEXT_LINE) {
            content += " po tekście: \"";
        }
        if (typeValue == LyricsTaskTypeValue.PREVIOUS_LINE) {
            content += " przed tekście: \"";
        }
        return content + questionLine + "\" występuje";
    }

    private String getContentEnglishQuestion(LyricsTaskTypeValue typeValue, Track track, String questionLine) {
        String content = "In the song \"" + track.getAuthor() + "\" - \"" + track.getName() + "\"";
        if (typeValue == LyricsTaskTypeValue.NEXT_LINE) {
            content += " after the text: \"";
        }
        if (typeValue == LyricsTaskTypeValue.PREVIOUS_LINE) {
            content += " before the text: \"";
        }
        return content + questionLine + "\" there is";
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
