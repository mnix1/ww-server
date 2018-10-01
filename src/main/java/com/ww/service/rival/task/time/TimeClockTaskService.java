package com.ww.service.rival.task.time;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.TimeTaskType;
import com.ww.model.container.rival.task.Interval;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.RandomHelper.randomInteger;
import static java.time.temporal.ChronoUnit.*;

@Service
public class TimeClockTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, TimeTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        if (typeValue == TimeTaskType.ANALOG_CLOCK_ADD || typeValue == TimeTaskType.ANALOG_CLOCK_SUBTRACT) {
            answersCount = Math.min(6, answersCount);
        }
        Instant questionDate = Instant.now().minusSeconds(randomInteger(100, 1000000));
        List<Interval> intervals = prepareIntervals(answersCount, remainedDifficulty);
        Interval correctInterval = intervals.get(0);
        List<Interval> wrongIntervals = intervals.subList(1, intervals.size());
        Instant correctDate = prepareDate(typeValue, questionDate, correctInterval);
        List<Instant> wrongDates = wrongIntervals.stream().map(interval -> prepareDate(typeValue, questionDate, interval)).collect(Collectors.toList());
        Question question = prepareQuestion(type, difficultyLevel, typeValue, questionDate, correctInterval);
        List<Answer> answers = prepareAnswers(typeValue, correctDate, wrongDates);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<Interval> prepareIntervals(int count, int difficulty) {
        Set<Interval> intervals = new HashSet<>(count);
        int calibration = difficultyCalibration(difficulty);
        while (intervals.size() < count) {
            Interval interval = Interval.randomInterval(calibration * 2, (calibration + 5) * 6, (calibration + 5) * 6);
            intervals.add(interval);
        }
        return new ArrayList<>(intervals);
    }

    private Instant prepareDate(TimeTaskType typeValue, Instant date, Interval interval) {
        if (typeValue == TimeTaskType.ANALOG_CLOCK_ADD || typeValue == TimeTaskType.DIGITAL_CLOCK_ADD) {
            if (interval.getHours() > 0) {
                date = date.plus(interval.getHours(), HOURS);
            }
            if (interval.getMinutes() > 0) {
                date = date.plus(interval.getMinutes(), MINUTES);
            }
            if (interval.getSeconds() > 0) {
                date = date.plus(interval.getSeconds(), SECONDS);
            }
        }
        if (typeValue == TimeTaskType.ANALOG_CLOCK_SUBTRACT || typeValue == TimeTaskType.DIGITAL_CLOCK_SUBTRACT) {
            if (interval.getHours() > 0) {
                date = date.minus(interval.getHours(), HOURS);
            }
            if (interval.getMinutes() > 0) {
                date = date.minus(interval.getMinutes(), MINUTES);
            }
            if (interval.getSeconds() > 0) {
                date = date.minus(interval.getSeconds(), SECONDS);
            }
        }
        return date;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, TimeTaskType typeValue, Instant date, Interval correctInterval) {
        Question question = new Question(type, difficultyLevel);
        question.setDateContent(date);
        if (typeValue == TimeTaskType.ANALOG_CLOCK_ADD || typeValue == TimeTaskType.DIGITAL_CLOCK_ADD) {
            question.setTextContentPolish("Która godzina będzie na zegarze po dodaniu " + correctInterval + "?");
            question.setTextContentEnglish("What is missing in the picture?");
        }
        if (typeValue == TimeTaskType.ANALOG_CLOCK_SUBTRACT || typeValue == TimeTaskType.DIGITAL_CLOCK_SUBTRACT) {
            question.setTextContentPolish("Która godzina będzie na zegarze po odjęciu " + correctInterval + "?");
            question.setTextContentEnglish("What is missing in the picture?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(TimeTaskType typeValue, Instant correctDate, List<Instant> wrongDates) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctDate);
        List<Answer> wrongAnswers = wrongDates.stream().map(date -> {
            Answer answer = new Answer(false);
            fillAnswerContent(typeValue, answer, date);
            return answer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(TimeTaskType typeValue, Answer answer, Instant date) {
        if (typeValue == TimeTaskType.ANALOG_CLOCK_ADD
                || typeValue == TimeTaskType.ANALOG_CLOCK_SUBTRACT
                || typeValue == TimeTaskType.DIGITAL_CLOCK_ADD
                || typeValue == TimeTaskType.DIGITAL_CLOCK_SUBTRACT) {
            answer.setDateContent(date);
        }
    }

}
