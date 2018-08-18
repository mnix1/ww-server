package com.ww.service.rival.task.chemistry;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ChemistryTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.ChemistryElement;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ChemistryElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class ChemistryTaskMinMaxService {

    @Autowired
    ChemistryElementRepository chemistryElementRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, ChemistryTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<ChemistryElement> elements = prepareElements(typeValue, answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, elements);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, ChemistryTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ChemistryTaskType.MAX_ATOMIC_MASS) {
            question.setTextContentPolish("Który z pierwiastków posiada największą masę atomową?");
            question.setTextContentEnglish("Which of the elements has the highest atomic mass?");
        }
        if (typeValue == ChemistryTaskType.MIN_ATOMIC_MASS) {
            question.setTextContentPolish("Który z pierwiastków posiada najmniejszą masę atomową?");
            question.setTextContentEnglish("Which of the elements has the lowest atomic mass?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(ChemistryTaskType typeValue, List<ChemistryElement> elements) {
        ChemistryElement correct = elements.get(0);
        for (int i = 1; i < elements.size(); i++) {
            ChemistryElement element = elements.get(i);
            if ((typeValue == ChemistryTaskType.MAX_ATOMIC_MASS && correct.getAtomicMass() < element.getAtomicMass())
                    || (typeValue == ChemistryTaskType.MIN_ATOMIC_MASS && correct.getAtomicMass() > element.getAtomicMass())) {
                correct = element;
            }
        }
        ChemistryElement finalCorrect = correct;
        return elements.stream().map(element -> {
            Answer answer = new Answer(element == finalCorrect);
            answer.setTextContentPolish(element.getNamePolish());
            answer.setTextContentEnglish(element.getNameEnglish());
            return answer;
        }).collect(Collectors.toList());
    }

    private List<ChemistryElement> prepareElements(ChemistryTaskType typeValue, int count) {
        List<ChemistryElement> allElements = chemistryElementRepository.findAll();
        List<ChemistryElement> elements = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (elements.size() < count) {
            ChemistryElement randomElement = randomElement(allElements);
            Double value = ChemistryTaskType.aboutAtomicMass(typeValue)
                    ? randomElement.getAtomicMass()
                    : 0;
            if (isValueDistanceEnough(value, values)) {
                elements.add(randomElement);
                values.add(value);
            }
        }
        return elements;
    }

    private boolean isValueDistanceEnough(Double value, List<Double> values) {
        return values.stream().noneMatch(e -> Math.abs(value - e) / value < 0.05);
    }


}
