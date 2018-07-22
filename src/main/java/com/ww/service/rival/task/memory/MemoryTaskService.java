package com.ww.service.rival.task.memory;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.MemoryTaskType;
import com.ww.model.container.MemoryObject;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.MemoryShape;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskColor;
import com.ww.repository.rival.task.category.MemoryShapeRepository;
import com.ww.repository.rival.task.category.TaskColorRepository;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElements;

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

    public Question generate(MemoryTaskType type) {
        List<MemoryObject> objects = prepareObjects(4);
        MemoryObject correctObject = randomElement(objects);
        List<MemoryObject> wrongObjects = new ArrayList<>(3);
        objects.forEach(memoryObject -> {
            if (memoryObject != correctObject) {
                wrongObjects.add(memoryObject);
            }
        });
        Question question = prepareQuestion(type, correctObject);
        List<Answer> answers = prepareAnswers(type, correctObject, wrongObjects);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(MemoryTaskType type, MemoryObject correctObject) {
        Question question = new Question();
        question.setCategory(Category.MEMORY);
        if (type == MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kolor miał objekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the color of the object " + correctObject.getKey() + "?");
        }
        if (type == MemoryTaskType.BORDER_COLOR_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kolor obramowania miał objekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the border color of the object " + correctObject.getKey() + "?");
        }
        if (type == MemoryTaskType.FONT_COLOR_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kolor czcionki miał objekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the font color of the object " + correctObject.getKey() + "?");
        }
        if (type == MemoryTaskType.SHAPE_FROM_FIGURE_KEY) {
            question.setTextContentPolish("Jaki kształt miał objekt " + correctObject.getKey() + "?");
            question.setTextContentEnglish("What was the font color of the object " + correctObject.getKey() + "?");
        }
        if (type == MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR) {
            question.setTextContentPolish("Który z obiektów miał " + correctObject.getBackgroundColor().getNamePolish() + " kolor?");
            question.setTextContentEnglish("Which of the objects was " + correctObject.getBackgroundColor().getNameEnglish() + "?");
        }
        if (type == MemoryTaskType.FIGURE_KEY_FROM_BORDER_COLOR) {
            question.setTextContentPolish("Obramowanie którego z objektów miało " + correctObject.getBorderColor().getNamePolish() + " kolor?");
            question.setTextContentEnglish("The border of which of the objects was " + correctObject.getBorderColor().getNameEnglish() + "?");
        }
        if (type == MemoryTaskType.FIGURE_KEY_FROM_FONT_COLOR) {
            question.setTextContentPolish("Czcionka którego z obiektów miała " + correctObject.getFontColor().getNamePolish() + " kolor?");
            question.setTextContentEnglish("The font of the name of which of the objects was " + correctObject.getFontColor().getNameEnglish() + "?");
        }
        if (type == MemoryTaskType.FIGURE_KEY_FROM_SHAPE) {
            question.setTextContentPolish("Kształt którego z obiektów to była " + correctObject.getShape().getNamePolish() + "?");
            question.setTextContentEnglish("The shape of which of the objects was " + correctObject.getShape().getNameEnglish() + "?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(MemoryTaskType type, MemoryObject correctObject, List<MemoryObject> wrongObjects) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(type, correctAnswer, correctObject);
        List<Answer> wrongAnswers = wrongObjects.stream().map(wrongObject -> {
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(type, wrongAnswer, wrongObject);
            return wrongAnswer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(MemoryTaskType type, Answer answer, MemoryObject object) {
        if (type == MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY) {
            answer.setTextContentPolish(object.getBackgroundColor().getNamePolish());
            answer.setTextContentEnglish(object.getBackgroundColor().getNameEnglish());
        }
        if (type == MemoryTaskType.BORDER_COLOR_FROM_FIGURE_KEY) {
            answer.setTextContentPolish(object.getBorderColor().getNamePolish());
            answer.setTextContentEnglish(object.getBorderColor().getNameEnglish());
        }
        if (type == MemoryTaskType.FONT_COLOR_FROM_FIGURE_KEY) {
            answer.setTextContentPolish(object.getFontColor().getNamePolish());
            answer.setTextContentEnglish(object.getFontColor().getNameEnglish());
        }
        if (type == MemoryTaskType.SHAPE_FROM_FIGURE_KEY) {
            answer.setTextContentPolish(object.getShape().getNamePolish());
            answer.setTextContentEnglish(object.getShape().getNameEnglish());
        }
        if(MemoryTaskType.answerFigureKey(type)){
            answer.setTextContent(object.getKey());
        }
    }

    private List<MemoryObject> prepareObjects(int count) {
        List<String> allKeys = new ArrayList<>();
        for (Character c : "QWERTYUIPASDFGHJKLZXCVBNM0123456789".toCharArray()) {
            allKeys.add(c.toString());
        }
        List<String> keys = randomElements(allKeys, count);
        List<MemoryShape> shapes = randomElements(memoryShapeRepository.findAll(), count);
        List<TaskColor> allColors = taskColorRepository.findAll();
        List<TaskColor> fontColors = randomElements(allColors, count);
        List<TaskColor> backgroundColors = randomElements(allColors, count);
        List<TaskColor> borderColors = randomElements(allColors, count);
        List<MemoryObject> figures = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            MemoryObject figure = new MemoryObject(keys.get(i), shapes.get(i), fontColors.get(i), backgroundColors.get(i), borderColors.get(i));
            figures.add(figure);
        }
        return figures;
    }

}
