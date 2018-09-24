package com.ww.service.rival.task.element;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.inside.task.Element;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.inside.category.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.model.constant.rival.task.type.ElementTaskType.aboutShellCount;

@Service
public class ElementOneCorrectTaskService {

    @Autowired
    ElementRepository elementRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ElementTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        List<Element> allElements = elementRepository.findAll();
        Element correctElement = randomElement(allElements);

        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctElement);
        List<Answer> answers = prepareAnswers(typeValue, correctElement, allElements, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, ElementTaskType typeValue, Element correctElement) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ElementTaskType.NAME_FROM_SYMBOL) {
            question.setTextContentPolish("Który z pierwiastków oznacza się jako " + correctElement.getSymbol());
            question.setTextContentEnglish("Which of the elements is marked as " + correctElement.getSymbol());
        }
        if (typeValue == ElementTaskType.SYMBOL_FROM_NAME) {
            question.setTextContentPolish("Symbolem pierwiastka " + correctElement.getNamePolish() + " jest");
            question.setTextContentEnglish("The symbol of the " + correctElement.getNameEnglish() + " is");
        }
        if (typeValue == ElementTaskType.NAME_FROM_NUMBER) {
            question.setTextContentPolish("Który z pierwiastków posiada liczbę atomową równą " + correctElement.getNumber());
            question.setTextContentEnglish("Which of the elements has atomic number equals " + correctElement.getNumber());
        }
        if (typeValue == ElementTaskType.SYMBOL_FROM_NUMBER) {
            question.setTextContentPolish("Podaj symbol pierwiastka, którego liczba atomowa to " + correctElement.getNumber());
            question.setTextContentEnglish("Give the symbol of the element whose atomic number is " + correctElement.getNumber());
        }
        if (question.getTextContentPolish() != null) {
            return question;
        }
        Integer shellCount = correctElement.getShellCount();
        Boolean multiShells = shellCount > 1;
        String questionLastPartEnglish = shellCount + (multiShells ? " electron shells" : " electron shell");
        if (typeValue == ElementTaskType.NAME_FROM_SHELL_COUNT) {
            question.setTextContentPolish("Podaj pierwiastek, którego liczba powłok elektronowych to " + shellCount);
            question.setTextContentEnglish("Which of the elements has " + questionLastPartEnglish);
        }
        if (typeValue == ElementTaskType.SYMBOL_FROM_SHELL_COUNT) {
            question.setTextContentPolish("Jaki jest symbol pierwiastka, którego liczba powłok elektronowych to " + shellCount);
            question.setTextContentEnglish("What is the symbol of the element that has " + questionLastPartEnglish);
        }
        if (typeValue == ElementTaskType.NUMBER_FROM_SHELL_COUNT) {
            question.setTextContentPolish("Jaką liczbę atomową może mieć pierwiastek, którego liczba powłok elektronowych to " + shellCount);
            question.setTextContentEnglish("What atomic number can have an element that has " + questionLastPartEnglish);
        }
        return question;
    }

    private List<Answer> prepareAnswers(ElementTaskType typeValue, Element correctElement, List<Element> allElements, int answersCount) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctElement);
        List<Answer> wrongAnswers = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        answerContents.add(correctAnswer.getTextContentEnglish());
        while (wrongAnswers.size() < answersCount - 1) {
            Element wrongElement = randomElement(allElements);
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(typeValue, wrongAnswer, wrongElement);
            if (!answerContents.contains(wrongAnswer.getTextContentEnglish())) {
                if (aboutShellCount(typeValue) && correctElement.getShellCount().equals(wrongElement.getShellCount())) {
                    continue;
                }
                wrongAnswers.add(wrongAnswer);
                answerContents.add(wrongAnswer.getTextContentEnglish());
            }
        }
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(ElementTaskType typeValue, Answer answer, Element element) {
        if (typeValue == ElementTaskType.NAME_FROM_SYMBOL
                || typeValue == ElementTaskType.NAME_FROM_NUMBER) {
            answer.setTextContentPolish(element.getNamePolish());
            answer.setTextContentEnglish(element.getNameEnglish());
        }
        if (typeValue == ElementTaskType.SYMBOL_FROM_NAME
                || typeValue == ElementTaskType.SYMBOL_FROM_NUMBER) {
            answer.setTextContentPolish(element.getSymbol());
            answer.setTextContentEnglish(element.getSymbol());
        }
        if (typeValue == ElementTaskType.NAME_FROM_SHELL_COUNT) {
            answer.setTextContentPolish(element.getNamePolish());
            answer.setTextContentEnglish(element.getNameEnglish());
        }
        if (typeValue == ElementTaskType.SYMBOL_FROM_SHELL_COUNT) {
            answer.setTextContentPolish(element.getSymbol());
            answer.setTextContentEnglish(element.getSymbol());
        }
        if (typeValue == ElementTaskType.NUMBER_FROM_SHELL_COUNT) {
            answer.setTextContentPolish(element.getNumber() + "");
            answer.setTextContentEnglish(element.getNumber() + "");
        }
    }

}
