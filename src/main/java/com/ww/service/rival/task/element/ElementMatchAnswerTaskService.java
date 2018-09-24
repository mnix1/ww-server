package com.ww.service.rival.task.element;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Element;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.RandomHelper.randomElement;

@Service
public class ElementMatchAnswerTaskService {

    @Autowired
    ElementRepository elementRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ElementTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        List<Element> elements = prepareElements(typeValue, answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, elements);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, ElementTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ElementTaskType.MAX_ATOMIC_MASS) {
            question.setTextContentPolish("Który z pierwiastków posiada największą masę atomową?");
            question.setTextContentEnglish("Which of the elements has the highest atomic mass?");
        }
        if (typeValue == ElementTaskType.MIN_ATOMIC_MASS) {
            question.setTextContentPolish("Który z pierwiastków posiada najmniejszą masę atomową?");
            question.setTextContentEnglish("Which of the elements has the lowest atomic mass?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(ElementTaskType typeValue, List<Element> elements) {
        Element correct = elements.get(0);
        for (int i = 1; i < elements.size(); i++) {
            Element element = elements.get(i);
            if ((typeValue == ElementTaskType.MAX_ATOMIC_MASS && correct.getAtomicMass() < element.getAtomicMass())
                    || (typeValue == ElementTaskType.MIN_ATOMIC_MASS && correct.getAtomicMass() > element.getAtomicMass())) {
                correct = element;
            }
        }
        Element finalCorrect = correct;
        return elements.stream().map(element -> {
            Answer answer = new Answer(element == finalCorrect);
            answer.setTextContentPolish(element.getNamePolish());
            answer.setTextContentEnglish(element.getNameEnglish());
            return answer;
        }).collect(Collectors.toList());
    }

    private List<Element> prepareElements(ElementTaskType typeValue, int count) {
        List<Element> allElements = elementRepository.findAll();
        List<Element> elements = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (elements.size() < count) {
            Element randomElement = randomElement(allElements);
            Double value = ElementTaskType.aboutAtomicMass(typeValue)
                    ? randomElement.getAtomicMass()
                    : 0;
            if (isValueDistanceEnough(value, values)) {
                elements.add(randomElement);
                values.add(value);
            }
        }
        return elements;
    }

}
