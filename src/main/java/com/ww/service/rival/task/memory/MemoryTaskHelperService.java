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
        memoryShapeRepository.save(new MemoryShape("CIRCLE","koło","circle"));
        memoryShapeRepository.save(new MemoryShape("TRIANGLE","trójkąt","triangle"));
        memoryShapeRepository.save(new MemoryShape("SQUARE","kwadrat","square"));
        memoryShapeRepository.save(new MemoryShape("RECTANGLE","prostokąt","rectangle"));
        memoryShapeRepository.save(new MemoryShape("PARALLELOGRAM","równoległobok","parallelogram"));
        memoryShapeRepository.save(new MemoryShape("TRAPEZE","trapez","trapeze"));
        memoryShapeRepository.save(new MemoryShape("PENTAGON","pięciokąt","pentagon"));
        memoryShapeRepository.save(new MemoryShape("HEXAGON","sześciokąt","hexagon"));
        memoryShapeRepository.save(new MemoryShape("STAR","gwiazda","star"));
    }
    
    public void initColors() {
        taskColorRepository.save(new TaskColor("#FFFFFF","biały","white"));
        taskColorRepository.save(new TaskColor("#808080","szary","gray"));
        taskColorRepository.save(new TaskColor("#000000","czarny","black"));
        taskColorRepository.save(new TaskColor("#FF0000","czerwony","red"));
        taskColorRepository.save(new TaskColor("#008000","zielony","green"));
        taskColorRepository.save(new TaskColor("#0000FF","niebieski","blue"));
        taskColorRepository.save(new TaskColor("#FFFF00","żółty","yellow"));
        taskColorRepository.save(new TaskColor("#FFA500","pomarańczowy","orange"));
        taskColorRepository.save(new TaskColor("#964B00","brązowy","brown"));
        taskColorRepository.save(new TaskColor("#B803FF","fioletowy","purple"));
        taskColorRepository.save(new TaskColor("#FFCCDD","różowy","pink"));
    }

}
