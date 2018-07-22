package com.ww.service.rival.task.memory;

import com.ww.model.entity.rival.task.TaskColor;
import com.ww.model.entity.rival.task.MemoryShape;
import com.ww.repository.rival.task.category.TaskColorRepository;
import com.ww.repository.rival.task.category.MemoryShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoryTaskHelperService {

    @Autowired
    MemoryShapeRepository memoryShapeRepository;

    @Autowired
    TaskColorRepository taskColorRepository;

    public void initShapes() {
        memoryShapeRepository.save(new MemoryShape("circle","koło","circle"));
        memoryShapeRepository.save(new MemoryShape("ellipse","elipsa","ellipse"));
        memoryShapeRepository.save(new MemoryShape("triangle","trójkąt","triangle"));
        memoryShapeRepository.save(new MemoryShape("square","kwadrat","square"));
        memoryShapeRepository.save(new MemoryShape("rectangle","prostokąt","rectangle"));
        memoryShapeRepository.save(new MemoryShape("parallelogram","równoległobok","parallelogram"));
        memoryShapeRepository.save(new MemoryShape("trapeze","trapez","trapeze"));
        memoryShapeRepository.save(new MemoryShape("pentagon","pięciokąt","pentagon"));
        memoryShapeRepository.save(new MemoryShape("hexagon","sześciokąt","hexagon"));
        memoryShapeRepository.save(new MemoryShape("star","gwiazda","star"));
    }
    
    public void initColors() {
        taskColorRepository.save(new TaskColor("#FFFFFF","biały","white"));
        taskColorRepository.save(new TaskColor("#808080","szary","gray"));
        taskColorRepository.save(new TaskColor("#000000","czarny","black"));
        taskColorRepository.save(new TaskColor("#990000","czerwony","red"));
        taskColorRepository.save(new TaskColor("#009900","zielony","green"));
        taskColorRepository.save(new TaskColor("#000099","niebieski","blue"));
        taskColorRepository.save(new TaskColor("#FFFF00","żółty","yellow"));
        taskColorRepository.save(new TaskColor("#FFA500","pomarańczowy","orange"));
        taskColorRepository.save(new TaskColor("#964B00","brązowy","brown"));
        taskColorRepository.save(new TaskColor("#B803FF","fioletowy","purple"));
        taskColorRepository.save(new TaskColor("#FFCCDD","różowy","pink"));
    }

}
