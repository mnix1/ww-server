package com.ww.database;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.social.ProfileRepository;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.service.MusicTaskService;
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
    MusicTaskService musicTaskService;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    private Random random = new SecureRandom();

    public void initTasks() {
        Question question = new Question();
        question.setCategory(Category.HISTORY);
        question.setContentEnglish("Give the year of founding the AKIT company?");
        question.setContentPolish("W którym roku założona została firma AKIT?");
        questionRepository.save(question);
        List<Answer> answers = new ArrayList<Answer>();
        for (int i = 2015; i <= 2018; i++) {
            Answer answer = new Answer();
            answer.setContent("" + i);
            answer.setCorrect(2018 == i);
            answer.setQuestion(question);
            answers.add(answer);
        }
        answerRepository.saveAll(answers);
    }

    public void initMusicTrack() {
        musicTaskService.addTrack("Łzy", "Agnieszka", "http://www.tekstowo.pl/piosenka,Lzy,agnieszka.html");
        musicTaskService.addTrack("Hej", "Teksański", "https://ising.pl/hey-teksanski-tekst");
        musicTaskService.addTrack("Ich Troje", "Zawsze z Tobą chciałbym być", "https://ising.pl/ich-troje-zawsze-z-toba-chcialbym-byc-przez-miesiac-tekst");
        musicTaskService.addTrack("Marek Grechuta", "Dni których nie znamy", "https://ising.pl/marek-grechuta-dni-ktorych-nie-znamy-tekst");
        musicTaskService.addTrack("Myslovitz", "Długość dźwięku samotności", "https://ising.pl/myslovitz-dlugosc-dzwieku-samotnosci-tekst");
        musicTaskService.addTrack("Lady Pank", "Warszawa", "https://ising.pl/lady-pank-stacja-warszawa-tekst");
        for (int i = 0; i < 100; i++) {
            musicTaskService.generateTask(Language.ALL, MusicTaskType.random());
        }
    }

}
