package com.ww.service.rival.task.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.MemoryTaskType;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.container.MemoryObject;
import com.ww.model.entity.rival.task.*;
import com.ww.repository.rival.task.category.MemoryShapeRepository;
import com.ww.repository.rival.task.category.TaskColorRepository;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElements;
import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class MemoryTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRendererService taskRendererService;

    @Autowired
    MemoryShapeRepository memoryShapeRepository;

    @Autowired
    TaskColorRepository taskColorRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        MemoryTaskType typeValue = MemoryTaskType.valueOf(type.getValue());
        int answersCount = 4;
        int animationObjectsCount = randomInteger(2, 2);
        List<MemoryObject> allObjects = prepareObjects(answersCount);
        MemoryObject correctObject = randomElement(allObjects);
        List<MemoryObject> wrongObjects = new ArrayList<>(answersCount - 1);
        allObjects.forEach(memoryObject -> {
            if (memoryObject != correctObject) {
                wrongObjects.add(memoryObject);
            }
        });
        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctObject);
        List<MemoryObject> animationObjects = new ArrayList<>(animationObjectsCount);
        animationObjects.add(correctObject);
        if (animationObjects.size() < animationObjectsCount) {
            animationObjects.addAll(wrongObjects.subList(0, animationObjectsCount - 1));
        }
        question.setAnimationContent(prepareAnimation(animationObjects));
        List<Answer> answers = prepareAnswers(typeValue, correctObject, wrongObjects, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private String prepareAnimation(List<MemoryObject> objects) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode objectsNode = mapper.createArrayNode();
        objects.forEach(object -> {
            object.writeToObjectNode(objectsNode.addObject());
        });
        try {
            return mapper.writeValueAsString(objectsNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, MemoryTaskType typeValue, MemoryObject correctObject) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kolor miał objekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the color of the object " + correctObject.getKey() + "?");
        }
        if (typeValue == MemoryTaskType.SHAPE_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kształt miał objekt " + correctObject.getKey() + "?");
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

    private List<Answer> prepareAnswers(MemoryTaskType typeValue, MemoryObject correctObject, List<MemoryObject> wrongObjects, int answersCount) {
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

    private void fillAnswerContent(MemoryTaskType typeValue, Answer answer, MemoryObject object) {
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

    private List<MemoryObject> prepareObjects(int count) {
        List<String> keys = prepareKeys(count);
        List<MemoryShape> shapes = randomElements(memoryShapeRepository.findAll(), count);
        List<TaskColor> allColors = taskColorRepository.findAll();
        List<TaskColor> backgroundColors = randomElements(allColors, count);
        List<MemoryObject> figures = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            MemoryObject figure = new MemoryObject(keys.get(i), shapes.get(i), backgroundColors.get(i));
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
