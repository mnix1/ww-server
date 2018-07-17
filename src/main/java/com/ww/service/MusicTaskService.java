package com.ww.service;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTaskType;
import com.ww.model.constant.rival.task.MusicTrackSource;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.category.MusicTrack;
import com.ww.repository.rival.task.category.MusicTrackRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
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

    public Boolean addTrack(String author, String name, String url) {
        return addTrack(author, name, url, Language.ALL);
    }

    public Boolean addTrack(String author, String name, String url, Language lang) {
        MusicTrack track = new MusicTrack();
        track.setAuthor(author);
        track.setName(name);
        track.setUrl(url);
        MusicTrackSource source = MusicTrackSource.fromUrl(url);
        track.setSource(source);
        track.setLang(lang);
        String content = downloadContent(url);
        if (content == null) {
            return false;
        }
        if (source == MusicTrackSource.TEKSTOWO) {
            content = transformContentTekstowo(content);
        }
        if (source == MusicTrackSource.ISING) {
            content = transformContentIsing(content);
        }
//        if (source == MusicTrackSource.GROOVE) {
//            content = transformContentGroove(content);
//        }
        track.setContent(content);
        musicTrackRepository.save(track);
        return true;
    }

    public QuestionDTO generateTask(Language lang, MusicTaskType type) {
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
        Question question = prepareQuestion(track, type, questionLine, lang);
        List<Answer> answers = prepareAnswers(allLines.get(correctAnswerIndex), wrongAnswerIndexes.stream().map(i -> allLines.get(i)).collect(Collectors.toList()));
        taskService.addTask(question, answers);
        return new QuestionDTO(question);
    }

    private Question prepareQuestion(MusicTrack track, MusicTaskType type, String questionLine, Language lang) {
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
            content += questionLine + "\" występuje wiersz:";
            question.setContentPolish(content);
        }
        if (Language.addEnglish(lang)) {
            // TODO add english
        }
        return question;
    }

    private List<Answer> prepareAnswers(String correctAnswerLine, List<String> wrongAnswerLines) {
        List<Answer> answers = new ArrayList<>();
        Answer correctAnswer = new Answer(true);
        correctAnswer.setContent(correctAnswerLine);
        answers.add(correctAnswer);
        answers.addAll(wrongAnswerLines.stream().map(s -> {
            Answer wrongAnswer = new Answer(false);
            wrongAnswer.setContent(s);
            return wrongAnswer;
        }).collect(Collectors.toList()));
        return answers;
    }

    private String downloadContent(String url) {
        try {
            if (url.contains("https")) {
                HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    content.append(inputLine);
                }
                return content.toString();
            } else {
                return IOUtils.toString(new URL(url), Charset.defaultCharset());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String transformContentTekstowo(String content) {
        content = content.substring(content.indexOf("<div class=\"song-text\">"));
        return content.substring(126, content.indexOf("<p>&nbsp;</p>") - 24)
                .replaceAll("\\\\", "")
                .replaceAll("[\";,./*]", "")
                .replaceAll(" \\?", "")
                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
                .replaceAll("<br >\n<br >\n", VERSE)
                .replaceAll("<br >\r\n", LINE)
                .replaceAll("<br >\n", LINE);
    }

    private String transformContentIsing(String content) {
        content = content.substring(content.indexOf("class=\"lyrics-original\""));
        return content.substring(71, content.indexOf("</p>"))
                .replaceAll("\\\\", "")
                .replaceAll("&quot;", "")
                .replaceAll(" \\?", "")
                .replaceAll("[\";,./*]", "")
                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
                .replaceAll("<br >\r\n", LINE)
                .replaceAll("<br >\n", LINE)
                .replaceAll("<br >", LINE);
    }

    //    private String transformContentGroove(String content) {
//        content = content.substring(content.indexOf("mid-content-text"));
//        return content.substring(18, content.indexOf("</div>") - 4)
//                .replaceAll("\\\\", "")
//                .replaceAll("[\";,./*]", "")
//                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
//                .replaceAll("<br >\r\n", LINE);
//    }

    private List<List<String>> trackVerseLineContent(String content) {
        List<String> verses = Arrays.asList(content.split(VERSE));
        int offset = verses.size() > 3 ? 3 : (verses.size() > 1 ? 1 : 0);
        List<List<String>> verseLines = verses.stream()
                .limit(verses.size() - offset)
                .map(verse -> Arrays.asList(verse.split(LINE)).stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()))
                .collect(Collectors.toList());
        return verseLines;
    }

    private List<String> trackAllLineContent(List<List<String>> verseLines) {
        return verseLines.stream().flatMap(strings -> strings.stream()).map(String::trim).collect(Collectors.toList());
    }

}
