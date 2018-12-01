package com.ww.service.rival.task.memory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ww.helper.JSONHelper;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.MemoryTaskType;
import com.ww.model.container.rival.task.Memory;
import com.ww.model.entity.inside.task.Color;
import com.ww.model.entity.inside.task.MemoryShape;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.inside.category.ColorRepository;
import com.ww.repository.inside.category.MemoryShapeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.*;

@Service
@AllArgsConstructor
public class MemoryTaskService {

    private final MemoryShapeRepository memoryShapeRepository;
    private final ColorRepository colorRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        MemoryTaskType typeValue = MemoryTaskType.valueOf(type.getValue());
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int animationObjectsCount = Math.min(Math.max(DifficultyLevel.answersCount(remainedDifficulty) / 2, 2), 5);
        remainedDifficulty -= remainedDifficulty / 2;
        int answersCount = Math.max(DifficultyLevel.answersCount(remainedDifficulty), animationObjectsCount);
        List<Memory> allObjects = prepareObjects(answersCount);
        Memory correctObject = randomElement(allObjects);
        List<Memory> wrongObjects = new ArrayList<>(answersCount - 1);
        allObjects.forEach(memory -> {
            if (memory != correctObject) {
                wrongObjects.add(memory);
            }
        });
        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctObject);
        List<Memory> animationObjects = new ArrayList<>(animationObjectsCount);
        animationObjects.add(correctObject);
        if (animationObjects.size() < animationObjectsCount) {
            animationObjects.addAll(wrongObjects.subList(0, animationObjectsCount - 1));
        }
        Collections.shuffle(animationObjects);
        question.setAnimationContent(prepareAnimation(animationObjects));
        List<Answer> answers = prepareAnswers(typeValue, correctObject, wrongObjects, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private String prepareAnimation(List<Memory> objects) {
        ArrayNode objectsNode = new ObjectMapper().createArrayNode();
        objects.forEach(object -> {
            object.writeToObjectNode(objectsNode.addObject());
        });
        return JSONHelper.toJSON(objectsNode);
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, MemoryTaskType typeValue, Memory correctObject) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kolor miał obiekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the color of the object " + correctObject.getKey() + "?");
        }
        if (typeValue == MemoryTaskType.SHAPE_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kształt miał obiekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the font color of the object " + correctObject.getKey() + "?");
        }
        if (typeValue == MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR || typeValue == MemoryTaskType.SHAPE_FROM_BACKGROUND_COLOR) {
            question.setTextContentPolish("Który z obiektów miał " + correctObject.getBackgroundColor().getNamePolish() + " kolor?");
            question.setTextContentEnglish("Which of the objects was " + correctObject.getBackgroundColor().getNameEnglish() + "?");
        }
        if (typeValue == MemoryTaskType.FIGURE_KEY_FROM_SHAPE) {
            question.setTextContentPolish("Który z wcześniej pokazanych obiektów to " + correctObject.getShape().getNamePolish() + "?");
            question.setTextContentEnglish("The shape of which of the objects was " + correctObject.getShape().getNameEnglish() + "?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(MemoryTaskType typeValue, Memory correctObject, List<Memory> wrongObjects, int answersCount) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctObject);
        List<Answer> wrongAnswers = wrongObjects.stream().limit(answersCount - 1).map(wrongObject -> {
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(typeValue, wrongAnswer, wrongObject);
            return wrongAnswer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(MemoryTaskType typeValue, Answer answer, Memory object) {
        if (typeValue == MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY) {
            answer.setTextContentPolish(object.getBackgroundColor().getNamePolish());
            answer.setTextContentEnglish(object.getBackgroundColor().getNameEnglish());
        }
        if (MemoryTaskType.answerShape(typeValue)) {
            answer.setTextContentPolish(object.getShape().getNamePolish());
            answer.setTextContentEnglish(object.getShape().getNameEnglish());
        }
        if (MemoryTaskType.answerFigureKey(typeValue)) {
            answer.setTextContent(object.getKey());
        }
    }

    private List<Memory> prepareObjects(int count) {
        List<String> keys = prepareKeys(count);
        List<MemoryShape> shapes = randomElements(memoryShapeRepository.findAll(), count);
        List<Color> allColors = colorRepository.findAll();
        List<Color> backgroundColors = randomElements(allColors, count);
        List<Memory> figures = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Memory figure = new Memory(keys.get(i), shapes.get(i), backgroundColors.get(i));
            figures.add(figure);
        }
        return figures;
    }

    private List<String> prepareKeys(int count) {
        //FIRST IMPLEMENTATION
//        List<String> allKeys = new ArrayList<>();
//        for (Character c : "QWERTYUIPASDFGHJKLZXCVBNM0123456789".toCharArray()) {
//            allKeys.add(c.toString());
//        }
//        return randomElements(allKeys, count);
        //SECOND IMPLEMENTATION
        Set<String> keys = new HashSet<>();
        while (keys.size() < count) {
            String key = "" + randomInteger(0, 9999);
            keys.add(key);
        }
        return new ArrayList<>(keys);
    }

}
