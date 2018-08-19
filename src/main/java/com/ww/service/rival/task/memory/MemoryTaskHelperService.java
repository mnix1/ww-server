package com.ww.service.rival.task.memory;

import com.ww.model.entity.rival.task.Color;
import com.ww.model.entity.rival.task.MemoryShape;
import com.ww.repository.rival.task.category.ColorRepository;
import com.ww.repository.rival.task.category.MemoryShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoryTaskHelperService {

    @Autowired
    MemoryShapeRepository memoryShapeRepository;

    @Autowired
    ColorRepository colorRepository;

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
        colorRepository.save(new Color("#FFFFFF","biały","white"));
        colorRepository.save(new Color("#888888","szary","gray"));
//        taskColorRepository.save(new Color("#000000","czarny","black"));
        colorRepository.save(new Color("#990000","czerwony","red"));
        colorRepository.save(new Color("#009900","zielony","green"));
        colorRepository.save(new Color("#000099","niebieski","blue"));
        colorRepository.save(new Color("#FFFF00","żółty","yellow"));
        colorRepository.save(new Color("#FFA500","pomarańczowy","orange"));
        colorRepository.save(new Color("#964B00","brązowy","brown"));
        colorRepository.save(new Color("#B803FF","fioletowy","purple"));
        colorRepository.save(new Color("#FFCCDD","różowy","pink"));
    }

}
